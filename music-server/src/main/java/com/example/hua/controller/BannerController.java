package com.example.hua.controller;

import com.example.hua.common.R;
import com.example.hua.model.domain.Banner;
import com.example.hua.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** 轮播图接口：负责首页 banner 的查询、新增、图片与跳转链接更新、删除 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/banner")
public class BannerController {

    private final BannerService bannerService;

    /** 查询全部轮播图。GET /banner/getAllBanner */
    @GetMapping("/getAllBanner")
    public R getAllBanner(){
        return R.success("成功获取轮播图与",bannerService.getAllBanner());
    }

    /** 新增轮播图，只上传图片。POST /banner/add */
    @PostMapping("/add")
    public R addBanner(@RequestParam("file") MultipartFile bannerFile) {
        return bannerService.addBanner(bannerFile);
    }

    /** 替换指定轮播图的图片。POST /banner/updatePic */
    @PostMapping("/updatePic")
    public R updateBannerPic(@RequestParam("file") MultipartFile bannerFile, @RequestParam("id") Integer id) {
        return bannerService.updateBannerPic(bannerFile, id);
    }

    /** 更新轮播图的跳转链接与标题。POST /banner/updateUrl */
    @PostMapping("/updateUrl")
    public R updateBannerUrl(@RequestBody Banner banner) {
        if (banner == null || banner.getId() == null) {
            return R.error("参数错误");
        }
        return bannerService.updateBannerMeta(banner.getId(), banner.getUrl(), banner.getTitle());
    }

    /** 删除轮播图。GET /banner/delete?id=1 */
    @GetMapping("/delete")
    public R deleteBanner(@RequestParam("id") Integer id) {
        return bannerService.deleteBanner(id);
    }
}
