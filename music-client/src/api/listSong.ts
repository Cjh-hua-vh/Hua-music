import { api } from "@/utils/request";
import { ensureArray } from "@/utils/api-response-data";
import type { HomeCardItem } from "@/api/models";

/** 查询歌单内的歌曲详情，歌单详情页的曲目列表使用。GET listSong/song/detail?songListId=:id */
export async function fetchSongDetailOfSongListId(songListId: string | number): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: `listSong/song/detail?songListId=${songListId}` });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}
