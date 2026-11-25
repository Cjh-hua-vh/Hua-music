import { api, getBlob, type ApiResponse } from "@/utils/request";
import { ensureArray } from "@/utils/api-response-data";
import type { HomeCardItem } from "@/api/models";

/** 查询某位歌手的全部歌曲，歌手详情页使用。GET song/singer/detail?singerId=:id */
export async function fetchSongListBySingerId(singerId: string | number): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: `song/singer/detail?singerId=${singerId}` });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}

/** 下载歌曲音频：走 getBlob 直接取二进制流 */
export async function fetchDownloadMusic(songUrl: string): Promise<Blob> {
  return getBlob(songUrl);
}

/** 按歌手名模糊搜索歌曲。GET song/singerName/detail?name=:keywords；注意：这里不吞异常 */
export async function fetchSongBySingerName(keywords: string): Promise<ApiResponse> {
  return await api({ url: `song/singerName/detail?name=${keywords}` });
}
