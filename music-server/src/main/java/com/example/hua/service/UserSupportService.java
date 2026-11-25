package com.example.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hua.common.R;
import com.example.hua.model.domain.UserSupport;
import com.example.hua.model.request.UserSupportRequest;

/** 评论点赞记录服务：对应 user_support 表 */
public interface UserSupportService extends IService<UserSupport> {

    /** 查询用户是否已给该评论点赞 */
    R isUserSupportComment(UserSupportRequest userSupportRequest);

    /** 新增点赞记录 */
    R insertCommentSupport(UserSupportRequest userSupportRequest);

    /** 删除点赞记录，即取消点赞 */
    R deleteCommentSupport(UserSupportRequest userSupportRequest);
}
