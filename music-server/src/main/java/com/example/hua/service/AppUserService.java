package com.example.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hua.common.R;
import com.example.hua.model.domain.AppUser;
import com.example.hua.model.request.AppUserRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/** 前台用户服务：负责注册、登录、资料与密码修改、头像替换 */
public interface AppUserService extends IService<AppUser> {

    /** 新增用户，不带头像，头像落默认图 */
    R addUser(AppUserRequest registryRequest);

    /** 新增用户并上传头像 */
    R addUserWithAvatar(AppUserRequest registryRequest, MultipartFile avatarFile);

    /** 修改用户资料，只更新昵称、性别、手机号、邮箱、生日、签名、地区 */
    R updateUserMsg(AppUserRequest updateRequest);

    /** 替换用户头像并回写图片地址 */
    R updateUserAvatar(MultipartFile avatarFile, int id);

    /** 修改密码，需校验旧密码 */
    R updatePassword(AppUserRequest updatePasswordRequest);

    /** 判断用户名是否已被占用 */
    boolean existUser(String username);

    /** 校验明文密码是否与库中存储的摘要一致 */
    boolean verityPasswd(String username, String password);

    /** 删除用户 */
    R deleteUser(Integer id);

    /** 查询全部用户，不分页 */
    R allUser();

    /** 分页查询用户 */
    R pageUser(Integer page, Integer size);

    /** 查询用户详情 */
    R userOfId(Integer id);

    /** 登录校验，成功后把用户信息写入 session */
    R loginStatus(AppUserRequest loginRequest, HttpSession session);
}
