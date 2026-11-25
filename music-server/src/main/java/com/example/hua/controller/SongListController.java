package com.example.hua.controller;

import com.example.hua.common.R;
import com.example.hua.model.request.SongListRequest;
import com.example.hua.service.SongListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/** 歌单接口：提供歌单的推荐、分页、详情、按标题/曲风检索 */
@RequiredArgsConstructor
@RestController
public class SongListController {

    private final SongListService songListService;

    /** 新增歌单。POST /songList/add */
    @PostMapping("/songList/add")
    public R addSongList(@RequestBody SongListRequest addSongListRequest) {
        return songListService.addSongList(addSongListRequest);
    }

    /** 删除歌单。GET /songList/delete?id=1 */
    @GetMapping("/songList/delete")
    public R deleteSongList(@RequestParam int id) {
        return songListService.deleteSongList(id);
    }

    /** 查询全部歌单。GET /songList */
    @GetMapping("/songList")
    public R allSongList() {
        return songListService.allSongList();
    }

    /** 首页推荐歌单。GET /songList/recommend?limit=10 */
    @GetMapping("/songList/recommend")
    public R recommendSongList(@RequestParam(defaultValue = "10") int limit) {
        int safeLimit = Math.max(limit, 1);
        return songListService.recommendSongList(safeLimit);
    }

    /** 分页查询歌单。GET /songList/page?page=1&amp */
    @GetMapping("/songList/page")
    public R pageSongList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return songListService.pageSongList(safePage, safeSize);
    }

    /** 查询歌单详情。GET /songList/detail?id=1 */
    @GetMapping("/songList/detail")
    public R songListOfId(@RequestParam int id) {
        return songListService.songListOfId(id);
    }

    /** 按标题模糊搜索歌单。GET /songList/likeTitle/detail?title= */
    @GetMapping("/songList/likeTitle/detail")
    public R songListOfLikeTitle(@RequestParam String title) {
        return songListService.likeTitle('%' + title + '%');
    }

    /** 按曲风查询歌单。GET /songList/style/detail?style= */
    @GetMapping("/songList/style/detail")
    public R songListOfStyle(@RequestParam String style) {
        return songListService.likeStyle('%' + style + '%');
    }

    /** 查询全部歌单曲风字典。GET /songList/style/list */
    @GetMapping("/songList/style/list")
    public R songListStyle() {
        return songListService.allSongListStyle();
    }

    /** 按曲风分页查询歌单。GET /songList/style/page/detail?style= */
    @GetMapping("/songList/style/page/detail")
    public R pageSongListOfStyle(
        @RequestParam String style,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return songListService.pageLikeStyle('%' + style + '%', safePage, safeSize);
    }

    /** 修改歌单信息。POST /songList/update */
    @PostMapping("/songList/update")
    public R updateSongListMsg(@RequestBody SongListRequest updateSongListRequest) {
        return songListService.updateSongListMsg(updateSongListRequest);
    }

    /** 替换歌单封面。POST /songList/img/update */
    @PostMapping("/songList/img/update")
    public R updateSongListPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return songListService.updateSongListImg(avatorFile,id);
    }
}
