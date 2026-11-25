import { api } from "@/utils/request";
import { routeQueryValue } from "@/utils/routeQuery";

/** 查询某首歌曲的评论。GET comment/song/detail?songId=:songId */
export function getCommentOfSongId(songId: unknown) {
  return api({ url: `comment/song/detail?songId=${routeQueryValue(songId)}` });
}

/** 查询某个歌单的评论。GET comment/songList/detail?songListId=:songListId */
export function getCommentOfSongListId(songListId: unknown) {
  return api({ url: `comment/songList/detail?songListId=${routeQueryValue(songListId)}` });
}

/** 删除评论。GET comment/delete?id=:id（后端为历史遗留的 GET 写法） */
export function deleteComment(id: string | number) {
  return api({ url: `comment/delete?id=${id}` });
}
