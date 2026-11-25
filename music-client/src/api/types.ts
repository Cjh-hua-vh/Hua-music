import type { AxiosRequestConfig } from "axios";
import type { ApiResponse } from "@/utils/request";

/** POST 提交 JSON 时的公共请求配置 */
export const jsonRequestConfig: AxiosRequestConfig = {
  headers: {
    "Content-Type": "application/json;charset=UTF-8",
  },
};

/** 把后端响应里的 type 转成 ElMessage 可用的类型 */
export function elMessageTypeFromResponse(
  r: Pick<ApiResponse, "success" | "type">,
): "success" | "warning" | "error" | "info" {
  const t = r.type;
  if (t === "success" || t === "warning" || t === "error" || t === "info") {
    return t;
  }
  return r.success ? "success" : "error";
}
