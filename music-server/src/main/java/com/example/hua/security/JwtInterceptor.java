package com.example.hua.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * JWT 拦截器：写操作（非 GET）必须携带有效 token。
 *
 * <p>GET 一律放行，因为前台游客要能浏览首页、歌单、歌曲详情；
 * 登录、注册、找回密码在放行名单内，否则拿不到 token。
 */
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private static final List<String> WHITE_LIST = Arrays.asList(
            "/admin/login/status",
            "/user/login/status",
            "/user/register",
            "/user/add",
            "/user/sendVerificationCode",
            "/user/resetPassword"
    );

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if ("OPTIONS".equalsIgnoreCase(method) || WHITE_LIST.contains(uri)) {
            return true;
        }
        if ("GET".equalsIgnoreCase(method)) {
            return true;
        }

        String token = jwtUtil.resolveToken(request.getHeader("Authorization"));
        Claims claims = token == null ? null : jwtUtil.parse(token);
        if (claims == null) {
            reject(response);
            return false;
        }

        request.setAttribute("jwtUid", claims.get("uid"));
        request.setAttribute("jwtRole", claims.get("role"));
        return true;
    }

    /** 返回与 R 结构一致的 401，前端拦截器会据此跳登录 */
    private void reject(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                "{\"code\":401,\"message\":\"登录已失效，请重新登录\",\"type\":\"error\",\"success\":false,\"data\":null}");
    }
}
