package com.example.hua.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/** MinIO 上传封装：按业务类型约定对象前缀 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioUploadService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /** 上传音频文件，对象名沿用原始文件名，落在 bucket 根目录 */
    public boolean uploadSongFile(MultipartFile file) {
        String objectName = file.getOriginalFilename();
        log.info("MinIO上传歌曲文件 - bucket: {}, objectName: {}, 文件大小: {} bytes",
                bucketName, objectName, file.getSize());
        return putObject(objectName, file);
    }

    /** 上传歌手头像 */
    public boolean uploadSingerImg(MultipartFile file, String objectFileName) {
        return putObject("singer/img/" + objectFileName, file);
    }

    /** 上传歌单封面 */
    public boolean uploadSongListImg(MultipartFile file, String objectFileName) {
        return putObject("songlist/" + objectFileName, file);
    }

    /** 上传歌曲封面 */
    public boolean uploadSongCoverImg(MultipartFile file, String objectFileName) {
        return putObject("singer/song/" + objectFileName, file);
    }

    /** 上传用户头像 */
    public boolean uploadAvatarImg(MultipartFile file, String objectFileName) {
        return putObject("img/avatorImages/" + objectFileName, file);
    }

    /** 执行上传：文件大小以 MultipartFile 提供的为准 */
    private boolean putObject(String objectName, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            long size = file.getSize() >= 0 ? file.getSize() : inputStream.available();
            log.info("MinIO putObject - bucket: {}, objectName: {}, size: {}, contentType: {}",
                    bucketName, objectName, size, file.getContentType());

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.info("MinIO 上传成功");
            return true;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("MinIO 上传失败", e);
            return false;
        }
    }
}
