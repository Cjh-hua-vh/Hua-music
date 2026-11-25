package com.example.hua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hua.model.domain.SongList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongListMapper extends BaseMapper<SongList> {

    List<SongList> selectRecommendedSongLists(@Param("limit") Integer limit);
}
