package com.example.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hua.common.R;
import com.example.hua.mapper.SongMapper;
import com.example.hua.model.domain.Song;
import com.example.hua.model.request.SongRequest;
import com.example.hua.service.MinioUploadService;
import com.example.hua.service.SongService;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.hua.constant.Constants;

@RequiredArgsConstructor
@Service
@Slf4j
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {

    private final SongMapper songMapper;

    private final MinioUploadService minioUploadService;

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    private static final String DEFAULT_SONG_PIC = "/img/songPic/tubiao.jpg";

    private void fillDefaultPic(Song song) {
        if (song != null && (song.getPic() == null || song.getPic().trim().isEmpty())) {
            song.setPic(DEFAULT_SONG_PIC);
        }
    }

    private void fillDefaultPic(java.util.List<Song> songs) {
        if (songs != null) {
            songs.forEach(this::fillDefaultPic);
        }
    }

    @Override
    public R allSong() {
        java.util.List<Song> songs = songMapper.selectList(null);
        fillDefaultPic(songs);
        return R.success(null, songs);
    }

    @Override
    public R pageSong(Integer page, Integer size) {
        Page<Song> pageInfo = new Page<>(page, size);
        songMapper.selectPage(pageInfo, null);
        fillDefaultPic(pageInfo.getRecords());
        Map<String, Object> data = new HashMap<>();
        data.put("records", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());
        data.put("page", page);
        data.put("size", size);
        data.put("hasMore", page * size < pageInfo.getTotal());
        return R.success(null, data);
    }

    @Override
    public R addSong(SongRequest addSongRequest, MultipartFile mpfile) {
        String fileName = mpfile.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            return R.error("文件名不能为空");
        }

        log.info("添加歌曲 - 文件名: {}, singerId: {}, 歌曲名: {}, 文件大小: {} bytes",
                fileName, addSongRequest.getSingerId(), addSongRequest.getName(), mpfile.getSize());

        Song song = new Song();
        BeanUtils.copyProperties(addSongRequest, song);
        String pic = "/img/songPic/tubiao.jpg";
        String storeUrlPath;

        boolean minioSuccess = minioUploadService.uploadSongFile(mpfile);
        log.info("MinIO上传结果: {}", minioSuccess);

        if (minioSuccess) {
            storeUrlPath = "/" + bucketName + "/" + fileName;
        } else {
            try {
                String basePath = System.getProperty("user.dir");
                if (basePath == null || basePath.isEmpty()) {
                    basePath = new File("").getAbsolutePath();
                }

                String localDir;
                if (basePath.endsWith("music-server") || basePath.contains("music-server" + File.separator)) {
                    localDir = basePath + File.separator + "song" + File.separator;
                } else {
                    localDir = basePath + File.separator + "music-server" + File.separator + "song" + File.separator;
                }
                log.info("本地回退路径: {}", localDir);

                File dir = new File(localDir);
                if (!dir.exists()) {
                    boolean created = dir.mkdirs();
                    log.info("创建目录结果: {}", created);
                }

                File targetFile = new File(localDir + fileName);
                try (InputStream inputStream = mpfile.getInputStream();
                     OutputStream outputStream = new FileOutputStream(targetFile)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                }
                storeUrlPath = "/song/" + fileName;
                log.info("本地回退成功 - 文件路径: {}", targetFile.getAbsolutePath());
            } catch (IOException e) {
                log.error("本地回退失败", e);
                return R.fatal("上传失败: " + e.getMessage());
            }
        }

        song.setCreateTime(new Date());
        song.setUpdateTime(new Date());
        song.setPic(pic);
        song.setUrl(storeUrlPath);
        song.setDuration(sanitizeDuration(song.getDuration()));

        log.info("准备插入数据库 - song: {}", song);
        int insertResult = songMapper.insert(song);
        log.info("数据库插入结果: {}", insertResult);

        if (insertResult > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", storeUrlPath);
            return R.success("上传成功", data);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R updateSongMsg(SongRequest updateSongRequest) {
        Song song = new Song();
        BeanUtils.copyProperties(updateSongRequest, song);
        if (songMapper.updateById(song) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R updateSongUrl(MultipartFile urlFile, int id) {
        Song existing = songMapper.selectById(id);
        if (existing != null && existing.getUrl() != null) {
            String prefix = "/" + bucketName + "/";
            if (existing.getUrl().startsWith(prefix)) {
                String oldObject = existing.getUrl().substring(prefix.length());
                if (!oldObject.contains("/")) {
                    try {
                        minioClient.removeObject(
                                RemoveObjectArgs.builder().bucket(bucketName).object(oldObject).build());
                    } catch (Exception e) {
                        log.warn("MinIO removeObject skipped for {}: {}", oldObject, e.getMessage());
                    }
                }
            }
        }

        String fileName = urlFile.getOriginalFilename();
        String storeUrlPath;
        if (minioUploadService.uploadSongFile(urlFile)) {
            storeUrlPath = "/" + bucketName + "/" + fileName;
        } else {
            try {
                String localDir = Constants.PROJECT_PATH + "/song/";
                File dir = new File(localDir);
                if (!dir.exists()) dir.mkdirs();
                urlFile.transferTo(new File(localDir + fileName));
                storeUrlPath = "/song/" + fileName;
            } catch (IOException e) {
                return R.fatal("更新失败");
            }
        }

        Song song = new Song();
        song.setId(id);
        song.setUrl(storeUrlPath);
        if (songMapper.updateById(song) > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", storeUrlPath);
            return R.success("更新成功", data);
        } else {
            return R.error("更新失败");
        }
    }

    @Override
    public R updateSongPic(MultipartFile urlFile, int id) {
        String originalFilename = urlFile.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = System.currentTimeMillis() + extension;
        String storeUrlPath;

        if (minioUploadService.uploadSongCoverImg(urlFile, fileName)) {
            storeUrlPath = "/" + bucketName + "/singer/song/" + fileName;
        } else {
            try {
                String basePath = System.getProperty("user.dir");
                if (basePath == null || basePath.isEmpty()) {
                    basePath = new File("").getAbsolutePath();
                }
                String localDir;
                if (basePath.endsWith("music-server") || basePath.contains("music-server" + File.separator)) {
                    localDir = basePath + File.separator + "img" + File.separator + "songPic" + File.separator;
                } else {
                    localDir = basePath + File.separator + "music-server" + File.separator + "img" + File.separator + "songPic" + File.separator;
                }
                log.info("更新歌曲图片 - 本地回退路径: {}", localDir);
                File dir = new File(localDir);
                if (!dir.exists()) {
                    boolean created = dir.mkdirs();
                    log.info("创建目录结果: {}", created);
                }
                File targetFile = new File(localDir + fileName);
                try (InputStream inputStream = urlFile.getInputStream();
                     OutputStream outputStream = new FileOutputStream(targetFile)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                }
                storeUrlPath = "/img/songPic/" + fileName;
                log.info("更新歌曲图片 - 本地回退成功 - 文件路径: {}", targetFile.getAbsolutePath());
            } catch (IOException e) {
                log.error("更新歌曲图片 - 本地回退失败", e);
                return R.fatal("上传失败: " + e.getMessage());
            }
        }

        Song song = new Song();
        song.setId(id);
        song.setPic(storeUrlPath);
        if (songMapper.updateById(song) > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", storeUrlPath);
            return R.success("上传成功", data);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R deleteSong(Integer id) {
        Song song = songMapper.selectById(id);

        if (songMapper.deleteById(id) > 0) {
            if (song != null && song.getUrl() != null) {
                String prefix = "/" + bucketName + "/";
                if (song.getUrl().startsWith(prefix)) {
                    String objectName = song.getUrl().substring(prefix.length());
                    if (!objectName.contains("/")) {
                        try {
                            minioClient.removeObject(
                                    RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
                        } catch (Exception e) {
                            log.warn("MinIO removeObject skipped for {}: {}", objectName, e.getMessage());
                        }
                    }
                }
            }
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R songOfSingerId(Integer singerId) {
        try {
            QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("singer_id", singerId);
            java.util.List<Song> songs = songMapper.selectList(queryWrapper);
            fillDefaultPic(songs);
            log.info("Query songs by singerId={}, count={}", singerId, songs != null ? songs.size() : 0);
            if (songs != null && !songs.isEmpty()) {
                return R.success("查询成功", songs);
            } else {
                return R.success("查询成功，但该歌手暂无歌曲", java.util.Collections.emptyList());
            }
        } catch (Exception e) {
            log.error("Failed to query songs by singerId={}", singerId, e);
            return R.fatal("查询失败: " + e.getMessage());
        }
    }

    @Override
    public R songOfId(Integer id) {
        Song song = songMapper.selectById(id);
        fillDefaultPic(song);
        return R.success(null, song);
    }

    @Override
    public R songOfSingerName(String name) {
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        java.util.List<Song> songs = songMapper.selectList(queryWrapper);
        fillDefaultPic(songs);
        return R.success(null, songs);
    }

    private Integer sanitizeDuration(Integer durationSeconds) {
        if (durationSeconds == null || durationSeconds < 0) {
            return 0;
        }
        return durationSeconds;
    }

}
