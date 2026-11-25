package com.example.hua.service;

import com.example.hua.common.R;
import com.example.hua.model.request.PasswordResetRequest;

/** 找回密码服务：验证码按邮箱生成并缓存（过期时间与发送冷却见 {@code app.password-reset.*}） */
public interface PasswordResetService {

    /** 向指定邮箱发送验证码：会先校验邮箱是否存在、是否处于发送冷却期 */
    R sendVerificationCode(String email);

    /** 校验验证码并重置密码：验证码一次性使用 */
    R resetPassword(PasswordResetRequest request);
}
