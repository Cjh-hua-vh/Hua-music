# Hua-music

A full-stack online music platform, consisting of a user-facing site and an admin console. It covers song playback, playlists, artists, comments and favorites.

> Built on top of the open-source project [Yin-Hongwei/music-website](https://github.com/Yin-Hongwei/music-website).
> See the [Chinese README](README.md) for the full list of changes made in this fork.

## Tech Stack

| Layer | Technology |
| --- | --- |
| Backend | Java 8, Spring Boot 2.6, MyBatis-Plus 3.5, MySQL 8, Redis, MinIO, JJWT |
| Client | Vue 3, TypeScript, Pinia, Element Plus, Vue CLI |
| Admin | Vue 3, TypeScript, Element Plus, Vue CLI |

## Quick Start

```bash
# 1. database
mysql -uroot -p -e "CREATE DATABASE hua_music DEFAULT CHARACTER SET utf8mb4;"
mysql -uroot -p --default-character-set=utf8mb4 hua_music < sql/hua_music.sql

# 2. backend (port 8888, needs Redis on 6379)
cd music-server && mvn spring-boot:run

# 3. frontend
cd music-client && npm install && npm run serve
cd music-manage && npm install && npm run serve
```

Audio files are **not** included in this repository (size and copyright). Put your own mp3 files into `music-server/song/`, see [the note there](music-server/song/README.md).

Default accounts: admin `admin / 123`, user `Yin33 / 123`.

## Changes in This Fork

- Rebranded from `Yin-*` to `Hua-*` across Java packages, Vue components and the database.
- Added JWT authentication (`JwtUtil` / `JwtInterceptor`); write operations now require a Bearer token.
- Unified password hashing on BCrypt, keeping transparent upgrade for legacy MD5/plaintext records.
- Implemented MinIO-with-local-fallback storage strategy and automatic bucket creation.
- Normalized comments and fixed several configuration inconsistencies.
