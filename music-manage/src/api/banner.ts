import { api } from "@/utils/request";

/** 查询全部轮播图。GET banner/getAllBanner */
export function getAllBanner() {
  return api({ url: "banner/getAllBanner" });
}

/** 修改轮播图的跳转链接与标题。POST banner/updateUrl */
export function updateBannerUrl(params: { id: string | number; url: string; title: string }) {
  return api({ method: "post", url: "banner/updateUrl", data: params });
}

/** 删除轮播图。GET banner/delete?id=:id（后端为历史遗留的 GET 写法） */
export function deleteBanner(id: string | number) {
  return api({ url: `banner/delete?id=${id}` });
}
