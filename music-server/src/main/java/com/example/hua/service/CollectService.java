package com.example.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hua.common.R;
import com.example.hua.model.domain.Collect;
import com.example.hua.model.request.CollectRequest;

/** 收藏服务：歌曲收藏与歌单收藏共用 collect 表 */
public interface CollectService extends IService<Collect> {

    /** 收藏歌曲 */
    R addCollection(CollectRequest addCollectRequest);

    /** 收藏歌单 */
    R addSongListCollection(CollectRequest addCollectRequest);

    /** 判断用户是否已收藏该歌曲 */
    R existSongId(CollectRequest isCollectRequest);

    /** 取消收藏歌曲，按用户 + 歌曲定位 */
    R deleteCollect(Integer userId, Integer songId);

    /** 判断用户是否已收藏该歌单 */
    R existSongListId(CollectRequest isCollectRequest);

    /** 取消收藏歌单，按用户 + 歌单定位 */
    R deleteSongListCollect(Integer userId, Integer songListId);

    /** 查询收藏了指定歌单的用户 */
    R collectionUsersOfSongList(Integer songListId);

    /** 查询用户收藏的歌曲 */
    R collectionOfUser(Integer userId);
}
