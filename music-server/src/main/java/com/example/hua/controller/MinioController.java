package com.example.hua.controller;

import com.example.hua.constant.Constants;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** 图片与音频的读取接口：统一按「MinIO → 本地 img/song 目录 → classpath 兜底」的顺序取文件 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class MinioController {

    @Value("${minio.bucket-name}")
    private String bucketName;

    private final MinioClient minioClient;

    /** 歌手头像。GET /{bucket}/singer/img/{fileName} */
    @GetMapping("/${minio.bucket-name}/singer/img/{fileName:.+}")
    public ResponseEntity<byte[]> getSingerImage(@PathVariable String fileName) {
        byte[] bytes = readFromMinio("singer/img/" + fileName);
        if (bytes == null) {
            bytes = readLocalFile(Paths.get(Constants.PROJECT_PATH, "img", "singerPic"), fileName);
        }
        return imageResponse(bytes);
    }

    /** 歌单封面。GET /{bucket}/songlist/{fileName} */
    @GetMapping("/${minio.bucket-name}/songlist/{fileName:.+}")
    public ResponseEntity<byte[]> getSongListImage(@PathVariable String fileName) {
        byte[] bytes = readFromMinio("songlist/" + fileName);
        if (bytes == null) {
            bytes = readLocalFile(Paths.get(Constants.PROJECT_PATH, "img", "songListPic"), fileName);
        }
        return imageResponse(bytes);
    }

    /** 歌曲封面。GET /{bucket}/singer/song/{fileName} */
    @GetMapping("/${minio.bucket-name}/singer/song/{fileName:.+}")
    public ResponseEntity<byte[]> getSongCoverImage(@PathVariable String fileName) {
        byte[] bytes = readFromMinio("singer/song/" + fileName);
        if (bytes == null) {
            bytes = readLocalFile(Paths.get(Constants.PROJECT_PATH, "img", "songPic"), fileName);
        }
        return imageResponse(bytes);
    }

    /** 音频文件。GET /{bucket}/{fileName} */
    @GetMapping("/${minio.bucket-name}/{fileName:.+}")
    public ResponseEntity<byte[]> getMusic(@PathVariable String fileName) {
        byte[] bytes = readFromMinio(fileName);
        if (bytes == null) {
            bytes = readLocalFile(Paths.get(Constants.PROJECT_PATH, "song"), fileName);
        }
        if (bytes == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    /** 用户头像。GET /img/avatorImages/{fileName} */
    @GetMapping("/img/avatorImages/{fileName:.+}")
    public ResponseEntity<byte[]> getAvatarImage(@PathVariable String fileName) {
        byte[] bytes = readFromMinio("img/avatorImages/" + fileName);
        if (bytes == null) {
            bytes = readLocalFile(Paths.get(Constants.PROJECT_PATH, "img", "avatorImages"), fileName);
        }
        if (bytes == null) {
            bytes = readClasspathStatic("static/img/avatorImages/" + fileName);
        }
        return imageResponse(bytes);
    }

    /** 组装图片响应，统一 Content-Type 为 image/jpeg */
    private ResponseEntity<byte[]> imageResponse(byte[] bytes) {
        if (bytes == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    /** 从 MinIO 读取对象，失败时返回 null 由调用方回退 */
    private byte[] readFromMinio(String objectKey) {
        try (InputStream in = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(objectKey).build())) {
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            log.debug("MinIO getObject skipped for {}: {}", objectKey, e.getMessage());
            return null;
        }
    }

    /** 从本地目录读取文件，带路径穿越防护 */
    private static byte[] readLocalFile(Path directory, String fileName) {
        if (fileName == null || fileName.isEmpty() || fileName.contains("..")) {
            return null;
        }
        try {
            Path base = directory.toAbsolutePath().normalize();
            Path target = base.resolve(fileName).normalize();
            if (!target.startsWith(base) || !Files.isRegularFile(target)) {
                return null;
            }
            return Files.readAllBytes(target);
        } catch (IOException e) {
            return null;
        }
    }

    /** 从 classpath 读取静态资源，仅用于默认头像兜底 */
    private static byte[] readClasspathStatic(String classpathLocation) {
        if (classpathLocation == null || classpathLocation.contains("..")) {
            return null;
        }
        ClassPathResource res = new ClassPathResource(classpathLocation);
        if (!res.exists()) {
            return null;
        }
        try (InputStream in = res.getInputStream()) {
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            return null;
        }
    }
}
