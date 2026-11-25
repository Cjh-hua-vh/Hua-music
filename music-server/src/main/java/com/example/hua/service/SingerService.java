package com.example.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hua.common.R;
import com.example.hua.model.domain.Singer;
import com.example.hua.model.request.SingerRequest;
import org.springframework.web.multipart.MultipartFile;

/** 歌手服务：歌手与曲风是多对多关系 */
public interface SingerService extends IService<Singer> {

    /** 新增歌手，同时写入曲风关联 */
    R addSinger(SingerRequest addSingerRequest);

    /** 修改歌手信息，同时重建曲风关联 */
    R updateSingerMsg(SingerRequest updateSingerRequest);

    /** 替换歌手头像 */
    R updateSingerPic(MultipartFile avatorFile, int id);

    /** 删除歌手 */
    R deleteSinger(Integer id);

    /** 查询全部歌手，不分页 */
    R allSinger();

    /** 分页查询歌手 */
    R pageSinger(Integer page, Integer size);

    /** 按名称查询歌手 */
    R singerOfName(String name);

    /** 按性别查询歌手 */
    R singerOfSex(Integer sex);

    /** 查询歌手曲风字典 */
    R allSingerStyle();
}
