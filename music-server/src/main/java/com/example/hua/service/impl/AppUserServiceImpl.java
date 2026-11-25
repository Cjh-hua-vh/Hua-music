package com.example.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hua.common.R;
import com.example.hua.mapper.AppUserMapper;
import com.example.hua.model.domain.AppUser;
import com.example.hua.model.request.AppUserRequest;
import com.example.hua.service.AppUserService;
import com.example.hua.service.MinioUploadService;
import com.example.hua.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.hua.constant.Constants;
import static com.example.hua.constant.Constants.SALT;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser>
        implements AppUserService {

    private static final String DEFAULT_AVATAR = "/img/avatorImages/user.jpg";

    private final AppUserMapper appUserMapper;

    private final PasswordEncoder passwordEncoder;

    private final MinioUploadService minioUploadService;

    private final JwtUtil jwtUtil;

    @Override
    public R addUser(AppUserRequest registryRequest) {
        if (this.existUser(registryRequest.getUsername())) {
            return R.warning("用户名已注册");
        }

        if (StringUtils.isNotBlank(registryRequest.getPhoneNum()) && this.existPhoneNum(registryRequest.getPhoneNum())) {
            return R.warning("该手机号已被注册");
        }

        if (StringUtils.isNotBlank(registryRequest.getEmail()) && this.existEmail(registryRequest.getEmail())) {
            return R.warning("该邮箱已被注册");
        }

        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(registryRequest, appUser);

        String password = passwordEncoder.encode(registryRequest.getPassword());
        appUser.setPassword(password);

        if (StringUtils.isBlank(appUser.getPhoneNum())) {
            appUser.setPhoneNum(null);
        }
        if (StringUtils.isBlank(appUser.getEmail())) {
            appUser.setEmail(null);
        }

        if (StringUtils.isBlank(appUser.getAvatar())) {
            appUser.setAvatar(DEFAULT_AVATAR);
        } else if (!appUser.getAvatar().startsWith("/")) {
            appUser.setAvatar("/" + appUser.getAvatar());
        }

        try {
            if (appUserMapper.insert(appUser) > 0) {
                return R.success("注册成功");
            } else {
                return R.error("注册失败");
            }
        } catch (DuplicateKeyException e) {
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("email")) {
                return R.warning("该邮箱已被注册");
            } else if (msg.contains("phone")) {
                return R.warning("该手机号已被注册");
            } else if (msg.contains("username")) {
                return R.warning("用户名已注册");
            }
            return R.warning("注册信息重复，请检查后重试");
        }
    }

    @Override
    public R addUserWithAvatar(AppUserRequest registryRequest, MultipartFile avatarFile) {
        if (avatarFile == null || avatarFile.isEmpty()) {
            return R.error("请上传头像图片");
        }

        if (this.existUser(registryRequest.getUsername())) {
            return R.warning("用户名已注册");
        }
        if (StringUtils.isNotBlank(registryRequest.getPhoneNum()) && this.existPhoneNum(registryRequest.getPhoneNum())) {
            return R.warning("该手机号已被注册");
        }
        if (StringUtils.isNotBlank(registryRequest.getEmail()) && this.existEmail(registryRequest.getEmail())) {
            return R.warning("该邮箱已被注册");
        }

        String originalFilename = avatarFile.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = System.currentTimeMillis() + extension;

        String imgPath;
        if (minioUploadService.uploadAvatarImg(avatarFile, fileName)) {
            imgPath = "/img/avatorImages/" + fileName;
        } else {
            try {
                String basePath = System.getProperty("user.dir");
                if (basePath == null || basePath.isEmpty()) {
                    basePath = new File("").getAbsolutePath();
                }

                String localDir;
                if (basePath.endsWith("music-server") || basePath.contains("music-server" + File.separator)) {
                    localDir = basePath + File.separator + "img" + File.separator + "avatorImages" + File.separator;
                } else {
                    localDir = basePath + File.separator + "music-server" + File.separator + "img" + File.separator + "avatorImages" + File.separator;
                }

                File dir = new File(localDir);
                if (!dir.exists()) {
                    boolean created = dir.mkdirs();
                }

                File targetFile = new File(localDir + fileName);
                try (InputStream inputStream = avatarFile.getInputStream();
                     OutputStream outputStream = new FileOutputStream(targetFile)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                }
                imgPath = "/img/avatorImages/" + fileName;
            } catch (IOException e) {
                return R.fatal("头像上传失败: " + e.getMessage());
            }
        }

        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(registryRequest, appUser);
        String password = passwordEncoder.encode(registryRequest.getPassword());
        appUser.setPassword(password);
        if (StringUtils.isBlank(appUser.getPhoneNum())) {
            appUser.setPhoneNum(null);
        }
        if (StringUtils.isBlank(appUser.getEmail())) {
            appUser.setEmail(null);
        }
        appUser.setAvatar(imgPath);

        try {
            if (appUserMapper.insert(appUser) > 0) {
                return R.success("注册成功");
            } else {
                return R.error("注册失败");
            }
        } catch (DuplicateKeyException e) {
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("email")) {
                return R.warning("该邮箱已被注册");
            } else if (msg.contains("phone")) {
                return R.warning("该手机号已被注册");
            } else if (msg.contains("username")) {
                return R.warning("用户名已注册");
            }
            return R.warning("注册信息重复，请检查后重试");
        }
    }

    @Override
    public R updateUserMsg(AppUserRequest updateRequest) {
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(updateRequest, appUser);

        if (appUserMapper.updateById(appUser) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R updatePassword(AppUserRequest updatePasswordRequest) {
        if (!this.verityPasswd(updatePasswordRequest.getUsername(), updatePasswordRequest.getOldPassword())) {
            return R.error("密码输入错误");
        }

        AppUser appUser = new AppUser();
        appUser.setId(updatePasswordRequest.getId());
        appUser.setPassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));

        if (appUserMapper.updateById(appUser) > 0) {
            return R.success("密码修改成功");
        } else {
            return R.error("密码修改失败");
        }
    }

    @Override
    public R updateUserAvatar(MultipartFile avatarFile, int id) {
        String fileName = System.currentTimeMillis() + avatarFile.getOriginalFilename();
        String imgPath;

        if (minioUploadService.uploadAvatarImg(avatarFile, fileName)) {
            imgPath = "/img/avatorImages/" + fileName;
        } else {
            try {
                String localDir = Constants.PROJECT_PATH + "/img/avatorImages/";
                File dir = new File(localDir);
                if (!dir.exists()) dir.mkdirs();
                avatarFile.transferTo(new File(localDir + fileName));
                imgPath = "/img/avatorImages/" + fileName;
            } catch (IOException e) {
                return R.fatal("上传失败");
            }
        }

        AppUser appUser = new AppUser();
        appUser.setId(id);
        appUser.setAvatar(imgPath);

        if (appUserMapper.updateById(appUser) > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", imgPath);
            return R.success("上传成功", data);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public boolean existUser(String username) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return appUserMapper.selectCount(queryWrapper) > 0;
    }

    private boolean existPhoneNum(String phoneNum) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_num", phoneNum);
        return appUserMapper.selectCount(queryWrapper) > 0;
    }

    private boolean existEmail(String email) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return appUserMapper.selectCount(queryWrapper) > 0;
    }

    private AppUser findAppUserByLoginAccount(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        String trimmed = account.trim();
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(w -> w.eq("username", trimmed).or().eq("email", trimmed));
        return appUserMapper.selectOne(queryWrapper);
    }

    private boolean matchesStoredPassword(AppUser appUser, String password) {
        if (appUser == null || StringUtils.isBlank(appUser.getPassword())) {
            return false;
        }
        String storedPassword = appUser.getPassword();

        if (passwordEncoder.matches(password, storedPassword)) {
            return true;
        }

        String legacyPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        if (legacyPassword.equals(storedPassword)) {
            AppUser update = new AppUser();
            update.setId(appUser.getId());
            update.setPassword(passwordEncoder.encode(password));
            appUserMapper.updateById(update);
            return true;
        }
        return false;
    }

    @Override
    public boolean verityPasswd(String username, String password) {
        AppUser appUser = findAppUserByLoginAccount(username);
        return matchesStoredPassword(appUser, password);
    }

    @Override
    public R deleteUser(Integer id) {
        if (appUserMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R allUser() {
        return R.success(null, appUserMapper.selectList(null));
    }

    @Override
    public R pageUser(Integer page, Integer size) {
        Page<AppUser> pageInfo = new Page<>(page, size);

        appUserMapper.selectPage(pageInfo, null);

        Map<String, Object> data = new HashMap<>();
        data.put("records", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());
        data.put("page", page);
        data.put("size", size);
        data.put("hasMore", page * size < pageInfo.getTotal());

        return R.success(null, data);
    }

    @Override
    public R userOfId(Integer id) {
        AppUser appUser = appUserMapper.selectById(id);
        return R.success(null, appUser);
    }

    @Override
    public R loginStatus(AppUserRequest loginRequest, HttpSession session) {
        String account = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (!this.verityPasswd(account, password)) {
            return R.error("用户名或密码错误");
        }

        AppUser appUser = findAppUserByLoginAccount(account);
        session.setAttribute("username", appUser.getUsername());
        appUser.setPassword(null);

        Map<String, Object> data = new HashMap<>();
        data.put("token", jwtUtil.generate(appUser.getId(), appUser.getUsername(), "user"));
        data.put("user", appUser);
        return R.success("登录成功", data);
    }
}
