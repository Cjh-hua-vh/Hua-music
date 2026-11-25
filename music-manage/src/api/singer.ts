import { api } from "@/utils/request";

/** 查询全部歌手。GET singer */
export function getAllSinger() {
  return api({ url: "singer" });
}

/** 新增歌手。POST singer/add */
export function setSinger(params: {
  name: string;
  sex: number | string;
  birth: string;
  location: string;
  introduction: string;
}) {
  return api({ method: "post", url: "singer/add", data: params });
}

/** 修改歌手信息。POST singer/update，按 id 更新 */
export function updateSingerMsg(params: {
  id: number | string;
  name: string;
  sex: number | string;
  birth: string;
  location: string;
  introduction: string;
}) {
  return api({ method: "post", url: "singer/update", data: params });
}

/** 删除歌手。GET singer/delete?id=:id（后端为历史遗留的 GET 写法） */
export function deleteSinger(id: string | number) {
  return api({ url: `singer/delete?id=${id}` });
}
