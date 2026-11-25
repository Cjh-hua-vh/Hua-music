package com.example.hua.controller;

import com.example.hua.common.R;
import com.example.hua.model.request.CommentRequest;
import com.example.hua.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 评论接口：歌曲评论与歌单评论共用 comment 表 */
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    /** 发表评论。POST /comment/add */
    @PostMapping("/comment/add")
    public R addComment(@RequestBody CommentRequest addCommentRequest) {
        return commentService.addComment(addCommentRequest);
    }

    /** 删除评论。GET /comment/delete?id=1 */
    @GetMapping("/comment/delete")
    public R deleteComment(@RequestParam Integer id) {
        return commentService.deleteComment(id);
    }

    /** 查询某首歌曲下的全部评论。GET /comment/song/detail?songId=1 */
    @GetMapping("/comment/song/detail")
    public R commentOfSongId(@RequestParam Integer songId) {
        return commentService.commentOfSongId(songId);
    }

    /** 查询某个歌单下的全部评论。GET /comment/songList/detail?songListId=1 */
    @GetMapping("/comment/songList/detail")
    public R commentOfSongListId(@RequestParam Integer songListId) {
        return commentService.commentOfSongListId(songListId);
    }

    /** 给评论点赞或取消点赞。POST /comment/like */
    @PostMapping("/comment/like")
    public R commentOfLike(@RequestBody CommentRequest upCommentRequest) {
        return commentService.updateCommentMsg(upCommentRequest);
    }
}
