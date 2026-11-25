import { api } from "@/utils/request";

/** 查询全部歌曲。GET song，不分页，后台表格前端分页使用 */
export function getAllSong() {
  return api({ url: "song" });
}

/** 查询某位歌手的全部歌曲。GET song/singer/detail?singerId=:id */
export function getSongOfSingerId(id: string | number) {
  return api({ url: `song/singer/detail?singerId=${id}` });
}

/** 查询歌曲详情。GET song/detail?id=:id */
export function getSongOfId(id: string | number) {
  return api({ url: `song/detail?id=${id}` });
}

/** 按歌手名模糊搜索歌曲。GET song/singerName/detail?name=:compositeName */
export function getSongOfSingerName(compositeName: string) {
  return api({ url: `song/singerName/detail?name=${compositeName}` });
}

/** 修改歌曲信息。POST song/update */
export function updateSongMsg(params: {
  id: number | string;
  singerId: number | string;
  name: string;
  introduction: string;
  lyric: string;
}) {
  return api({ method: "post", url: "song/update", data: params });
}

/** 删除歌曲。DELETE song/delete?id=:id */
export function deleteSong(id: string | number) {
  return api({ method: "delete", url: `song/delete?id=${id}` });
}
