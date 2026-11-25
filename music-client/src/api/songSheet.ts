import { api, type ApiResponse } from "@/utils/request";
import { emptyPagedResult, ensureArray, normalizePagedResult, type PagedResult } from "@/utils/api-response-data";
import type { HomeCardItem, SongStyleItem } from "@/api/models";

/** 获取歌单列表，取全量后在前端截断。GET songList；注意：limit 不是后端分页参数 */
export async function fetchSongList(limit = 10): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: "songList" });
    return ensureArray<HomeCardItem>(result?.data).slice(0, limit);
  } catch {
    return [];
  }
}

/** 获取首页推荐歌单。GET songList/recommend?limit=:limit */
export async function fetchRecommendedSongList(limit = 10): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: `songList/recommend?limit=${limit}` });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}

/** 获取全部歌单，不分页，歌单页首屏与后台下拉框使用。GET songList */
export async function fetchSongSheetList(): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: "songList" });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}

/** 分页获取歌单。GET songList/page?page=:page&size=:size */
export async function fetchSongSheetListPage(page: number, size: number): Promise<PagedResult<HomeCardItem>> {
  try {
    const result = await api({ url: `songList/page?page=${page}&size=${size}` });
    return normalizePagedResult<HomeCardItem>(result?.data, page, size);
  } catch {
    return emptyPagedResult(page, size);
  }
}

/** 按曲风获取歌单（不分页）。GET songList/style/detail?style=:style */
export async function fetchSongSheetListByStyle(style: string): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: `songList/style/detail?style=${style}` });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}

/** 按曲风分页获取歌单，歌单分类页使用。GET songList/style/page/detail?style=:style&page=:page&size=:size */
export async function fetchSongSheetListByStylePage(
  style: string,
  page: number,
  size: number,
): Promise<PagedResult<HomeCardItem>> {
  try {
    const result = await api({
      url: `songList/style/page/detail?style=${style}&page=${page}&size=${size}`,
    });
    return normalizePagedResult<HomeCardItem>(result?.data, page, size);
  } catch {
    return emptyPagedResult(page, size);
  }
}

/** 获取歌单曲风字典，用于筛选栏。GET songList/style/list */
export async function fetchSongSheetStyleList(): Promise<SongStyleItem[]> {
  try {
    const result = await api({ url: "songList/style/list" });
    const styles = ensureArray<Record<string, unknown>>(result?.data);
    return styles
      .map((item) => ({
        id: Number(item.id) || 0,
        name: typeof item.name === "string" ? item.name.trim() : "",
      }))
      .filter((item) => item.name.length > 0);
  } catch {
    return [{ id: 0, name: "全部歌单" }];
  }
}

/** 按标题模糊搜索歌单。GET songList/likeTitle/detail?title=:keywords；注意：不吞异常 */
export async function fetchSongListByLikeTitle(keywords: string): Promise<ApiResponse> {
  return await api({ url: `songList/likeTitle/detail?title=${keywords}` });
}
