package com.example.hua.controller;

import com.example.hua.common.R;
import com.example.hua.model.request.CollectRequest;
import com.example.hua.service.CollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 收藏接口：歌曲收藏与歌单收藏共用 collect 表 */
@RequiredArgsConstructor
@RestController
public class CollectController {

    private final CollectService collectService;

    /** 收藏歌曲。POST /collection/add */
    @PostMapping("/collection/add")
    public R addCollection(@RequestBody CollectRequest addCollectRequest) {
        return collectService.addCollection(addCollectRequest);
    }

    /** 收藏歌单。POST /collection/songList/add */
    @PostMapping("/collection/songList/add")
    public R addSongListCollection(@RequestBody CollectRequest addCollectRequest) {
        return collectService.addSongListCollection(addCollectRequest);
    }

    /** 取消收藏歌曲。DELETE /collection/delete?userId=1&amp */
    @DeleteMapping("/collection/delete")
    public R deleteCollection(@RequestParam Integer userId, @RequestParam Integer songId) {
        return collectService.deleteCollect(userId, songId);
    }

    /** 取消收藏歌单。DELETE /collection/songList/delete?userId=1&amp */
    @DeleteMapping("/collection/songList/delete")
    public R deleteSongListCollection(@RequestParam Integer userId, @RequestParam Integer songListId) {
        return collectService.deleteSongListCollect(userId, songListId);
    }

    /** 判断用户是否已收藏某首歌曲。POST /collection/status */
    @PostMapping("/collection/status")
    public R isCollection(@RequestBody CollectRequest isCollectRequest) {
        return collectService.existSongId(isCollectRequest);
    }

    /** 判断用户是否已收藏某个歌单。POST /collection/songList/status */
    @PostMapping("/collection/songList/status")
    public R isSongListCollection(@RequestBody CollectRequest isCollectRequest) {
        return collectService.existSongListId(isCollectRequest);
    }

    /** 查询用户收藏的歌曲列表。GET /collection/detail?userId=1 */
    @GetMapping("/collection/detail")
    public R collectionOfUser(@RequestParam Integer userId) {
        return collectService.collectionOfUser(userId);
    }

    /** 查询收藏了指定歌单的用户。GET /collection/songList/collectors?songListId=1 */
    @GetMapping("/collection/songList/collectors")
    public R collectionUsersOfSongList(@RequestParam Integer songListId) {
        return collectService.collectionUsersOfSongList(songListId);
    }
}
