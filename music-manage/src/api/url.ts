import { getBaseURL } from "@/utils/request";

/** 把库里存的相对路径拼成可直接访问的完整图片地址 */
export function attachImageUrl(url: string | undefined | null): string {
  const rawUrl = typeof url === "string" ? url.trim() : "";

  let fallbackPath = "/img/songPic/tubiao.jpg";
  if (rawUrl && rawUrl.includes("avatorImages")) {
    fallbackPath = "/img/avatorImages/user.jpg";
  } else if (rawUrl && rawUrl.includes("singerPic")) {
    fallbackPath = "/img/singerPic/tubiao.jpg";
  }

  const normalizedPath = rawUrl || fallbackPath;

  if (/^https?:\/\//i.test(normalizedPath)) {
    return normalizedPath;
  }

  const baseUrl = (getBaseURL() || "").replace(/\/+$/, "");
  const path = normalizedPath.startsWith("/") ? normalizedPath : `/${normalizedPath}`;
  return `${baseUrl}${path}`;
}

/** el-upload 新增轮播图的上传地址，multipart 字段名需为 file。 */
export function getBannerAddUploadUrl(): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/banner/add`;
}

/** el-upload 替换轮播图图片的上传地址，multipart 字段名需为 file，id 走 query。 */
export function getBannerUpdatePicUploadUrl(id: string | number): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/banner/updatePic?id=${id}`;
}

/** el-upload 替换歌曲音频的上传地址，上限 20MB。 */
export function getSongUrlUpdateUploadUrl(id: string | number): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/song/url/update?id=${id}`;
}

/** el-upload 替换歌曲封面的上传地址。 */
export function getSongImgUpdateUploadUrl(id: string | number): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/song/img/update?id=${id}`;
}

/** el-upload 替换歌手头像的上传地址。 */
export function getSingerAvatarUpdateUploadUrl(id: string | number): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/singer/avatar/update?id=${id}`;
}

/** el-upload 替换歌单封面的上传地址。 */
export function getSongListImgUpdateUploadUrl(id: string | number): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/songList/img/update?id=${id}`;
}

/** el-upload 新增歌曲的上传地址，multipart 字段名需为 file，其余字段按表单同名提交。 */
export function getSongAddUrl(): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/song/add`;
}
