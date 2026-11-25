package com.example.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hua.common.R;
import com.example.hua.model.domain.Banner;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/** 轮播图服务：新增与替换图片统一经 MinioUploadService 转存 */
public interface BannerService extends IService<Banner> {

    /** 查询全部轮播图 */
    List<Banner> getAllBanner();

    /** 新增轮播图，只上传图片 */
    R addBanner(MultipartFile bannerFile);

    /** 替换指定轮播图的图片 */
    R updateBannerPic(MultipartFile bannerFile, Integer id);

    /** 更新跳转链接与标题，不涉及文件 */
    R updateBannerMeta(Integer id, String url, String title);

    /** 删除轮播图记录 */
    R deleteBanner(Integer id);
}
