package com.example.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hua.common.R;
import com.example.hua.model.domain.Song;
import com.example.hua.model.request.SongRequest;
import org.springframework.web.multipart.MultipartFile;

/** 歌曲服务：新增与替换文件统一经 MinioUploadService 转存 */
public interface SongService extends IService<Song> {

    /** 新增歌曲，同时上传音频文件 */
    R addSong(SongRequest addSongRequest, MultipartFile mpfile);

    /** 修改歌曲文本信息（名称、简介、歌词、歌手） */
    R updateSongMsg(SongRequest updateSongRequest);

    /** 替换歌曲音频 */
    R updateSongUrl(MultipartFile urlFile, int id);

    /** 替换歌曲封面 */
    R updateSongPic(MultipartFile urlFile, int id);

    /** 删除歌曲 */
    R deleteSong(Integer id);

    /** 查询全部歌曲，不分页 */
    R allSong();

    /** 分页查询歌曲 */
    R pageSong(Integer page, Integer size);

    /** 查询某位歌手的全部歌曲 */
    R songOfSingerId(Integer singerId);

    /** 查询歌曲详情 */
    R songOfId(Integer id);

    /** 按歌手名模糊查询歌曲 */
    R songOfSingerName(String name);
}
