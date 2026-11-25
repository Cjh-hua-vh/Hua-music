package com.example.hua.controller;

import com.example.hua.common.R;
import com.example.hua.model.request.SongRequest;
import com.example.hua.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;

/** 歌曲接口：提供歌曲的增删改查、按歌手检索、分页 */
@RequiredArgsConstructor
@RestController
public class SongController {

    private final SongService songService;

    /** 上传体积限制：单个文件与整个请求均限制 20MB */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(System.getProperty("java.io.tmpdir"));
        factory.setMaxFileSize(DataSize.of(20, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(20, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

    /** 新增歌曲。POST /song/add */
    @PostMapping("/song/add")
    public R addSong(SongRequest addSongRequest, @RequestParam("file") MultipartFile mpfile) {
        return songService.addSong(addSongRequest,mpfile);
    }

    /** 删除歌曲。DELETE /song/delete?id=1 */
    @DeleteMapping("/song/delete")
    public R deleteSong(@RequestParam int id) {
        return songService.deleteSong(id);
    }

    /** 查询全部歌曲。GET /song */
    @GetMapping("/song")
    public R allSong() {
        return songService.allSong();
    }

    /** 分页查询歌曲。GET /song/page?page=1&amp */
    @GetMapping("/song/page")
    public R pageSong(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return songService.pageSong(safePage, safeSize);
    }

    /** 查询歌曲详情。GET /song/detail?id=1 */
    @GetMapping("/song/detail")
    public R songOfId(@RequestParam int id) {
        return songService.songOfId(id);
    }

    /** 查询某位歌手的全部歌曲。GET /song/singer/detail?singerId=1 */
    @GetMapping("/song/singer/detail")
    public R songOfSingerId(@RequestParam(value = "singerId", required = true) Integer singerId) {
        if (singerId == null) {
            return R.error("singerId参数不能为空");
        }
        return songService.songOfSingerId(singerId);
    }

    /** 按歌手名模糊搜索歌曲。GET /song/singerName/detail?name= */
    @GetMapping("/song/singerName/detail")
    public R songOfSingerName(@RequestParam String name) {
        return songService.songOfSingerName('%' + name + '%');
    }

    /** 修改歌曲信息。POST /song/update */
    @PostMapping("/song/update")
    public R updateSongMsg(@RequestBody SongRequest updateSongRequest) {
        return songService.updateSongMsg(updateSongRequest);
    }

    /** 替换歌曲封面。POST /song/img/update */
    @PostMapping("/song/img/update")
    public R updateSongPic(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        return songService.updateSongPic(urlFile, id);
    }

    /** 替换歌曲音频文件。POST /song/url/update */
    @PostMapping("/song/url/update")
    public R updateSongUrl(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        return songService.updateSongUrl(urlFile, id);
    }
}
