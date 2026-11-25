package com.example.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hua.common.R;
import com.example.hua.model.domain.ListSong;
import com.example.hua.model.request.ListSongRequest;

import java.util.List;

/** 歌单-歌曲关联服务：只维护 song_sheet_song 关联表 */
public interface ListSongService extends IService<ListSong> {

    /** 向歌单添加歌曲 */
    R addListSong(ListSongRequest addListSongRequest);

    /** 修改关联记录 */
    R updateListSongMsg(ListSongRequest updateListSongRequest);

    /** 从歌单移除歌曲：只按 songId 删除 */
    R deleteListSong(Integer songId);

    /** 查询全部关联记录 */
    List<ListSong> allListSong();

    /** 查询歌单内的关联记录，只含 songId / songSheetId */
    R listSongOfSongId(Integer songListId);

    /** 查询歌单内的歌曲详情，供后台表格渲染 */
    R songDetailOfSongListId(Integer songListId);
}
