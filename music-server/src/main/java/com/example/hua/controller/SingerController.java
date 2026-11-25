package com.example.hua.controller;

import com.example.hua.common.R;
import com.example.hua.model.request.SingerRequest;
import com.example.hua.service.SingerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/** 歌手接口：提供歌手列表、分页、按名称/性别检索、曲风字典查询 */
@RequiredArgsConstructor
@RestController
public class SingerController {

    private final SingerService singerService;

    /** 新增歌手。POST /singer/add */
    @PostMapping("/singer/add")
    public R addSinger(@RequestBody SingerRequest addSingerRequest) {
        return singerService.addSinger(addSingerRequest);
    }

    /** 删除歌手。GET /singer/delete?id=1 */
    @GetMapping("/singer/delete")
    public R deleteSinger(@RequestParam int id) {
        return singerService.deleteSinger(id);
    }

    /** 查询全部歌手。GET /singer */
    @GetMapping("/singer")
    public R allSinger() {
        return singerService.allSinger();
    }

    /** 分页查询歌手。GET /singer/page?page=1&amp */
    @GetMapping("/singer/page")
    public R pageSinger(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return singerService.pageSinger(safePage, safeSize);
    }

    /** 按名称查询歌手。GET /singer/name/detail?name= */
    @GetMapping("/singer/name/detail")
    public R singerOfName(@RequestParam String name) {
        return singerService.singerOfName(name);
    }

    /** 按性别查询歌手。GET /singer/sex/detail?sex=1 */
    @GetMapping("/singer/sex/detail")
    public R singerOfSex(@RequestParam int sex) {
        return singerService.singerOfSex(sex);
    }

    /** 查询全部歌手曲风字典。GET /singer/style/list */
    @GetMapping("/singer/style/list")
    public R singerStyleList() {
        return singerService.allSingerStyle();
    }

    /** 修改歌手信息。POST /singer/update */
    @PostMapping("/singer/update")
    public R updateSingerMsg(@RequestBody SingerRequest updateSingerRequest) {
        return singerService.updateSingerMsg(updateSingerRequest);
    }

    /** 替换歌手头像。POST /singer/avatar/update */
    @PostMapping("/singer/avatar/update")
    public R updateSingerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return singerService.updateSingerPic(avatorFile, id);
    }
}
