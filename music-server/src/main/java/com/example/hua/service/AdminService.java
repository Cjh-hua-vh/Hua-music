package com.example.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hua.common.R;
import com.example.hua.model.domain.Admin;
import com.example.hua.model.request.AdminRequest;

import javax.servlet.http.HttpSession;

/** 管理员账号服务*/
public interface AdminService extends IService<Admin> {

    /** 校验管理员账号密码，成功后把管理员信息写入 session */
    R verityPasswd(AdminRequest adminRequest, HttpSession session);
}
