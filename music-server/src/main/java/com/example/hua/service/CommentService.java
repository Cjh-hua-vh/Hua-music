package com.example.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hua.common.R;
import com.example.hua.model.domain.Comment;
import com.example.hua.model.request.CommentRequest;

/** 评论服务：歌曲评论与歌单评论共用 comment 表 */
public interface CommentService extends IService<Comment> {

    /** 新增评论 */
    R addComment(CommentRequest addCommentRequest);

    /** 更新评论，目前仅用于写入最新的点赞数 */
    R updateCommentMsg(CommentRequest upCommentRequest);

    /** 删除评论 */
    R deleteComment(Integer id);

    /** 查询某首歌曲的评论 */
    R commentOfSongId(Integer songId);

    /** 查询某个歌单的评论 */
    R commentOfSongListId(Integer songListId);
}
