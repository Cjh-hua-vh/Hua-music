import { api } from "@/utils/request";

/** 新增歌单。POST songList/add */
export function setSongList(params: { title: string; introduction: string; style: string }) {
  return api({ method: "post", url: "songList/add", data: params });
}

/** 查询全部歌单。GET songList */
export function getSongList() {
  return api({ url: "songList" });
}

/** 修改歌单信息。POST songList/update，按 id 更新 */
export function updateSongListMsg(params: {
  id: number | string;
  title: string;
  introduction: string;
  style: string;
}) {
  return api({ method: "post", url: "songList/update", data: params });
}

/** 删除歌单。GET songList/delete?id=:id（后端为历史遗留的 GET 写法） */
export function deleteSongList(id: string | number) {
  return api({ url: `songList/delete?id=${id}` });
}
