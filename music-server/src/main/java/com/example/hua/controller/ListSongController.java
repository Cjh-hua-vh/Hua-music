package com.example.hua.controller;

import com.example.hua.common.R;
import com.example.hua.model.request.ListSongRequest;
import com.example.hua.service.ListSongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 歌单-歌曲关联接口（后台「歌单内容」管理） */
@RequiredArgsConstructor
@RestController
public class ListSongController {

    private final ListSongService listSongService;

    /** 向歌单添加歌曲。POST /listSong/add */
    @PostMapping("/listSong/add")
    public R addListSong(@RequestBody ListSongRequest addListSongRequest) {
        return listSongService.addListSong(addListSongRequest);
    }

    /** 从歌单移除歌曲。DELETE /listSong/delete?songId=1（只按 songId 删，会波及所有歌单） */
    @DeleteMapping("/listSong/delete")
    public R deleteListSong(@RequestParam int songId) {
        return listSongService.deleteListSong(songId);
    }

    /** 查询歌单内的关联记录（只有 songId / songSheetId）。GET /listSong/detail?songListId=1 */
    @GetMapping("/listSong/detail")
    public R listSongOfSongId(@RequestParam int songListId) {
        return listSongService.listSongOfSongId(songListId);
    }

    /** 查询歌单内的歌曲详情，供后台表格直接渲染。GET /listSong/song/detail?songListId=1 */
    @GetMapping("/listSong/song/detail")
    public R songDetailOfSongListId(@RequestParam int songListId) {
        return listSongService.songDetailOfSongListId(songListId);
    }

    /** 修改关联记录。POST /listSong/update */
    @PostMapping("/listSong/update")
    public R updateListSongMsg(@RequestBody ListSongRequest updateListSongRequest) {
        return listSongService.updateListSongMsg(updateListSongRequest);
    }
}
