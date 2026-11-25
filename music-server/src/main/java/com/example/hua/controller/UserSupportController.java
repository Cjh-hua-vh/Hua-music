package com.example.hua.controller;

import com.example.hua.common.R;
import com.example.hua.model.request.UserSupportRequest;
import com.example.hua.service.UserSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 评论点赞记录接口：对应 user_support 表 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/userSupport")
public class UserSupportController {

    private final UserSupportService userSupportService;

    /** 查询用户是否已给某条评论点赞。POST /userSupport/test */
    @PostMapping("/test")
    public R isUserSupportComment(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.isUserSupportComment(userSupportRequest);
    }

    /** 新增点赞记录。POST /userSupport/insert */
    @PostMapping("/insert")
    public R insertCommentSupport(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.insertCommentSupport(userSupportRequest);
    }

    /** 取消点赞，删除对应记录。POST /userSupport/delete */
    @PostMapping("/delete")
    public R deleteCommentSupport(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.deleteCommentSupport(userSupportRequest);
    }
}
