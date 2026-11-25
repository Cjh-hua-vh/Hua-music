import { api } from "@/utils/request";
import { ensureArray } from "@/utils/api-response-data";
import type { HomeCardItem, SingerStyleItem } from "@/api/models";

/** 获取歌手列表，取全量后在前端截断。GET singer；注意：limit 只是前端 slice 上限 */
export async function fetchSingerList(limit = 10): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: "singer" });
    return ensureArray<HomeCardItem>(result?.data).slice(0, limit);
  } catch {
    return [];
  }
}

/** 获取全部歌手。GET singer */
export async function fetchAllSingerList(): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: "singer" });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}

/** 按性别筛选歌手。GET singer/sex/detail?sex=:sex */
export async function fetchSingerListBySex(sex: string | number): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: `singer/sex/detail?sex=${sex}` });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}

/** 获取歌手曲风字典，用于筛选栏。GET singer/style/list */
export async function fetchSingerStyleList(): Promise<SingerStyleItem[]> {
  try {
    const result = await api({ url: "singer/style/list" });
    const styles = ensureArray<Record<string, unknown>>(result?.data);
    return styles
      .map((item) => ({
        id: Number(item.id) || 0,
        name: typeof item.name === "string" ? item.name.trim() : "",
        type: Number(item.type),
      }))
      .filter((item) => item.name.length > 0 && !Number.isNaN(item.type));
  } catch {
    return [{ id: 0, name: "全部歌手", type: -1 }];
  }
}
