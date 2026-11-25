package com.example.hua.controller;

import com.example.hua.common.R;
import com.example.hua.model.request.AppUserRequest;
import com.example.hua.model.request.PasswordResetRequest;
import com.example.hua.security.LoginAttemptService;
import com.example.hua.service.AppUserService;
import com.example.hua.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/** 前台用户接口：覆盖注册（含带头像注册）、登录、找回密码、资料与密码修改、头像替换 */
@RequiredArgsConstructor
@RestController
public class AppUserController {

    private final AppUserService appUserService;

    private final LoginAttemptService loginAttemptService;

    private final PasswordResetService passwordResetService;

    /** 新增用户（不带头像）。POST /user/add */
    @PostMapping("/user/add")
    public R addUser(@RequestBody AppUserRequest registryRequest) {
        return appUserService.addUser(registryRequest);
    }

    /** 注册用户（带头像）。POST /user/register */
    @PostMapping("/user/register")
    public R registerUser(@RequestParam("avatar") MultipartFile avatarFile,
                          @RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("sex") Byte sex,
                          @RequestParam(value = "phoneNum", required = false) String phoneNum,
                          @RequestParam(value = "email", required = false) String email,
                          @RequestParam("birth") String birth,
                          @RequestParam(value = "introduction", required = false) String introduction,
                          @RequestParam(value = "location", required = false) String location) {
        AppUserRequest registryRequest = new AppUserRequest();
        registryRequest.setUsername(username);
        registryRequest.setPassword(password);
        registryRequest.setSex(sex);
        registryRequest.setPhoneNum(phoneNum);
        registryRequest.setEmail(email);
        registryRequest.setIntroduction(introduction);
        registryRequest.setLocation(location);
        return appUserService.addUserWithAvatar(registryRequest, avatarFile);
    }

    /** 发送找回密码的验证码。GET /user/sendVerificationCode?email=xxx */
    @GetMapping("/user/sendVerificationCode")
    public R sendVerificationCode(@RequestParam String email) {
        return passwordResetService.sendVerificationCode(email);
    }

    /** 校验验证码并重置密码。POST /user/resetPassword */
    @PostMapping("/user/resetPassword")
    public R resetPassword(@RequestBody PasswordResetRequest request) {
        return passwordResetService.resetPassword(request);
    }

    /** 用户登录。POST /user/login/status */
    @PostMapping("/user/login/status")
    public R loginStatus(@RequestBody AppUserRequest loginRequest, HttpSession session, HttpServletRequest request) {
        String account = loginRequest.getUsername();
        if (loginAttemptService.isBlocked(account, request)) {
            long remain = loginAttemptService.getRemainingSeconds(account, request);
            return R.error("登录失败次数过多，请" + remain + "秒后重试");
        }

        R result = appUserService.loginStatus(loginRequest, session);
        if (Boolean.TRUE.equals(result.getSuccess())) {
            loginAttemptService.onLoginSuccess(account, request);
        } else {
            loginAttemptService.onLoginFailure(account, request);
        }
        return result;
    }

    /** 查询全部用户。GET /user */
    @GetMapping("/user")
    public R allUser() {
        return appUserService.allUser();
    }

    /** 分页查询用户。GET /user/page?page=1&amp */
    @GetMapping("/user/page")
    public R pageUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return appUserService.pageUser(safePage, safeSize);
    }

    /** 查询用户详情。GET /user/detail?id=1 */
    @GetMapping("/user/detail")
    public R userOfId(@RequestParam int id) {
        return appUserService.userOfId(id);
    }

    /** 删除用户。GET /user/delete?id=1 */
    @GetMapping("/user/delete")
    public R deleteUser(@RequestParam int id) {
        return appUserService.deleteUser(id);
    }

    /** 修改用户资料（不含密码与头像）。POST /user/update */
    @PostMapping("/user/update")
    public R updateUserMsg(@RequestBody AppUserRequest updateRequest) {
        return appUserService.updateUserMsg(updateRequest);
    }

    /** 修改密码。POST /user/updatePassword */
    @PostMapping("/user/updatePassword")
    public R updatePassword(@RequestBody AppUserRequest updatePasswordRequest) {
        return appUserService.updatePassword(updatePasswordRequest);
    }

    /** 替换用户头像。POST /user/avatar/update */
    @PostMapping("/user/avatar/update")
    public R updateUserPic(@RequestParam("file") MultipartFile avatarFile, @RequestParam("id") int id) {
        return appUserService.updateUserAvatar(avatarFile, id);
    }
}
