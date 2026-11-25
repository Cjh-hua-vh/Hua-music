package com.example.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hua.common.R;
import com.example.hua.constant.Constants;
import com.example.hua.mapper.SingerMapper;
import com.example.hua.mapper.SingerStyleMapper;
import com.example.hua.model.domain.Singer;
import com.example.hua.model.domain.SingerStyle;
import com.example.hua.model.request.SingerRequest;
import com.example.hua.service.MinioUploadService;
import com.example.hua.service.SingerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer> implements SingerService {

    private final SingerMapper singerMapper;

    private final SingerStyleMapper singerStyleMapper;

    private final MinioUploadService minioUploadService;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public R updateSingerMsg(SingerRequest updateSingerRequest) {
        Singer singer = new Singer();
        BeanUtils.copyProperties(updateSingerRequest, singer);

        if (singerMapper.updateById(singer) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R updateSingerPic(MultipartFile avatorFile, int id) {
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String imgPath;

        if (minioUploadService.uploadSingerImg(avatorFile, fileName)) {
            imgPath = "/" + bucketName + "/singer/img/" + fileName;
        } else {
            try {
                String localDir = Constants.PROJECT_PATH + "/img/singerPic/";
                File dir = new File(localDir);
                if (!dir.exists()) dir.mkdirs();
                avatorFile.transferTo(new File(localDir + fileName));
                imgPath = "/img/singerPic/" + fileName;
            } catch (IOException e) {
                return R.fatal("上传失败");
            }
        }

        Singer singer = new Singer();
        singer.setId(id);
        singer.setPic(imgPath);
        if (singerMapper.updateById(singer) > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", imgPath);
            return R.success("上传成功", data);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R deleteSinger(Integer id) {
        if (singerMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R allSinger() {
        return R.success(null, singerMapper.selectList(null));
    }

    @Override
    public R pageSinger(Integer page, Integer size) {
        Page<Singer> pageInfo = new Page<>(page, size);
        singerMapper.selectPage(pageInfo, null);
        Map<String, Object> data = new HashMap<>();
        data.put("records", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());
        data.put("page", page);
        data.put("size", size);
        data.put("hasMore", page * size < pageInfo.getTotal());
        return R.success(null, data);
    }

    @Override
    public R addSinger(SingerRequest addSingerRequest) {
        Singer singer = new Singer();
        BeanUtils.copyProperties(addSingerRequest, singer);
        String pic = "/img/avatorImages/user.jpg";
        singer.setPic(pic);
        if (singerMapper.insert(singer) > 0) {
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @Override
    public R singerOfName(String name) {
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        return R.success(null, singerMapper.selectList(queryWrapper));
    }

    @Override
    public R singerOfSex(Integer sex) {
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("sex", sex);
        return R.success(null, singerMapper.selectList(queryWrapper));
    }

    @Override
    public R allSingerStyle() {
        List<Map<String, Object>> styleList = new ArrayList<>();

        Map<String, Object> allStyle = new HashMap<>();
        allStyle.put("id", 0);
        allStyle.put("name", "全部歌手");
        allStyle.put("type", -1);
        styleList.add(allStyle);

        List<SingerStyle> dbStyles = singerStyleMapper.selectList(new QueryWrapper<SingerStyle>().orderByAsc("id"));

        for (SingerStyle item : dbStyles) {
            if (item == null || item.getName() == null || item.getName().trim().isEmpty() || item.getType() == null) {
                continue;
            }
            Map<String, Object> styleItem = new HashMap<>();
            styleItem.put("id", item.getId());
            styleItem.put("name", item.getName().trim());
            styleItem.put("type", item.getType());
            styleList.add(styleItem);
        }
        return R.success(null, styleList);
    }
}
