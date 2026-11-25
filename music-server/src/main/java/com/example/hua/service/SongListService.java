package com.example.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hua.common.R;
import com.example.hua.model.domain.SongList;
import com.example.hua.model.request.SongListRequest;
import org.springframework.web.multipart.MultipartFile;

/** 歌单服务：负责歌单本身的信息与封面 */
public interface SongListService extends IService<SongList> {

    /** 新增歌单，同时写入曲风关联 */
    R addSongList(SongListRequest addSongListRequest);

    /** 修改歌单信息 */
    R updateSongListMsg(SongListRequest updateSongListRequest);

    /** 替换歌单封面 */
    R updateSongListImg(MultipartFile avatorFile, int id);

    /** 删除歌单 */
    R deleteSongList(Integer id);

    /** 查询全部歌单，不分页 */
    R allSongList();

    /** 分页查询歌单 */
    R pageSongList(Integer page, Integer size);

    /** 查询歌单详情 */
    R songListOfId(Integer id);

    /** 按标题模糊查询歌单 */
    R likeTitle(String title);

    /** 按曲风模糊查询歌单 */
    R likeStyle(String style);

    /** 按曲风分页查询歌单 */
    R pageLikeStyle(String style, Integer page, Integer size);

    /** 查询歌单曲风字典 */
    R allSongListStyle();

    /** 首页推荐歌单 */
    R recommendSongList(Integer limit);
}
