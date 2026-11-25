import { api } from "@/utils/request";

/** 管理员登录。POST admin/login/status */
export async function getLoginStatus(params: { username: string; password: string }) {
  return api({ method: "post", url: "admin/login/status", data: params });
}
