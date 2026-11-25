import { api } from "@/utils/request";
import { ensureArray } from "@/utils/api-response-data";
import type { BannerItem } from "@/api/models";

/** 获取首页轮播图列表。GET banner/getAllBanner */
export async function fetchBannerList(): Promise<BannerItem[]> {
  try {
    const result = await api({ url: "banner/getAllBanner" });
    return ensureArray<BannerItem>(result?.data);
  } catch {
    return [];
  }
}
