import { api } from "@/utils/request";
import { routeQueryValue } from "@/utils/routeQuery";

/** 向歌单添加歌曲。POST listSong/add */
export function setListSong(params: { songId: number; songListId: string | number }) {
  return api({ method: "post", url: "listSong/add", data: params });
}

/** 查询歌单内的关联记录（只有 songId / songSheetId）。GET listSong/detail?songListId=:id */
export function getListSongOfSongId(songListId: unknown) {
  return api({ url: `listSong/detail?songListId=${routeQueryValue(songListId)}` });
}

/** 从歌单移除歌曲。DELETE listSong/delete?songId=:songId；注意：后端只按 songId 删除 */
export function deleteListSong(songId: string | number) {
  return api({ method: "delete", url: `listSong/delete?songId=${songId}` });
}
