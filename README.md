# Hua-music 音乐网站

一个前后端分离的在线音乐平台，包含面向用户的音乐站与面向管理员的后台，覆盖歌曲播放、歌单、歌手、评论、收藏等完整业务闭环。

> 在其基础上完成了品牌重构、鉴权体系、密码安全与存储层的改造，详见文末「二次开发说明」。

## 技术栈

| 分层 | 技术选型 |
| --- | --- |
| 后端 | Java 8、Spring Boot 2.6、MyBatis-Plus 3.5、MySQL 8、Redis、MinIO、JJWT |
| 用户端 | Vue 3、TypeScript、Pinia、Element Plus、Vue CLI（webpack） |
| 管理端 | Vue 3、TypeScript、Element Plus、Vue CLI（webpack） |
| 构建 | Maven、npm |

## 功能

**用户端**

- 首页轮播、推荐歌单、歌手分类浏览
- 歌曲播放：播放/暂停、进度拖拽、音量、播放队列、歌词滚动
- 歌单详情与收藏、歌曲收藏
- 评论发表、评论点赞
- 用户注册、登录、找回密码（邮箱验证码）、个人中心

**管理端**

- 歌手管理：新增、编辑、头像上传、按性别/曲风筛选
- 歌曲管理：新增、编辑、音频与封面上传、分页检索
- 歌单管理：新增、编辑、封面上传、歌单曲目维护
- 轮播图管理、用户管理、评论与收藏查看

## 项目结构

```
music-website-master/
├── music-server/          后端服务
│   ├── src/main/java/com/example/hua
│   ├── src/main/resources
│   ├── song/              音频目录（未入库）
│   └── img/               图片资源
├── music-client/          用户端
├── music-manage/          管理端
└── sql/                   数据库脚本
```

## 本地运行

### 1. 准备数据库

```bash
mysql -uroot -p -e "CREATE DATABASE hua_music DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
mysql -uroot -p --default-character-set=utf8mb4 hua_music < sql/hua_music.sql
```

然后修改 `music-server/src/main/resources/application-dev.properties` 中的数据源账号密码。

### 2. 启动后端

```bash
cd music-server
mvn spring-boot:run
```

默认端口 **8888**。依赖本地 Redis（默认 6379，用于首页轮播缓存）；MinIO 可不启动，上传会自动回退到本地目录。

### 3. 启动前端

```bash
cd music-client && npm install && npm run serve
cd music-manage && npm install && npm run serve
```

### 4. 补充音频文件

`music-server/song/` 目录体积过大且涉及版权，**未纳入版本库**。自行放入 mp3 即可，文件名需与数据库中 `song.url` 保持一致，说明见 [music-server/song/README.md](music-server/song/README.md)。

### 默认账号

| 端 | 账号 | 密码 |
| --- | --- | --- |
| 管理端 | admin | 123 |
| 用户端 | Yin33 | 123 |
## 已知限制

- 后台查询类接口（GET）尚未做角色校验，JWT 中已写入 `role` 但未启用；
- 部分写操作沿用 GET 方法（历史遗留），未改为 DELETE/POST；
- 单元测试覆盖较少。
