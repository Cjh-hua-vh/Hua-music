import axios, { AxiosRequestConfig } from "axios";

import router from "../router";

/** 后端统一响应体 R 的前端映射 */
export type ApiResponse<T = unknown> = {
  code: number | string;

  message: string;

  success: boolean;

  data?: T;

  type?: string;
};

/** 后端地址，由 vue.config.js 的 DefinePlugin 在编译期注入，改地址只需改 vue.config.js。 */
const BASE_URL = process.env.VUE_APP_NODE_HOST || process.env.NODE_HOST;

/** 全站唯一的 axios 实例 */
const request = axios.create({
  baseURL: BASE_URL,

  timeout: 5000,

  withCredentials: true,

  headers: {
    "Content-Type": "application/json;charset=UTF-8",
  },
});

/** token 存放键名，登录页写入、这里读取 */
export const TOKEN_KEY = "hua_token";

export function getToken(): string {
  return localStorage.getItem(TOKEN_KEY) || "";
}

export function setToken(token: string) {
  localStorage.setItem(TOKEN_KEY, token);
}

export function clearToken() {
  localStorage.removeItem(TOKEN_KEY);
}

/** 请求拦截器：自动附带 Bearer token，后端 JwtInterceptor 用它校验写操作 */
request.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers = config.headers ?? {};
    (config.headers as Record<string, string>).Authorization = `Bearer ${token}`;
  }
  return config;
});

/** 响应拦截器：HTTP 层不成功统一转为 reject */
request.interceptors.response.use(
  (response) => {
    if (response.status === 200) {
      return response;
    }
    return Promise.reject(response);
  },

  (error) => {
    const res = error.response;

    if (!res) {
      return Promise.reject(error);
    }

    if (res.status) {
      switch (res.status) {

        case 401:
          clearToken();
          router.replace({ path: "/", query: {} });
          break;

        case 403:
          setTimeout(() => {
            router.replace({ path: "/", query: {} });
          }, 1000);
          break;

        case 404:
          break;
      }
      return Promise.reject(res);
    }

    return Promise.reject(error);
  },
);

/** 返回后端根地址，用于拼 el-upload 的 action 等非 axios 场景。 */
export function getBaseURL() {
  return BASE_URL;
}

export type HttpMethod = "get" | "post" | "delete" | "put";

export interface ApiRequestOptions {
  /** 请求方法，默认 get。 */
  method?: HttpMethod;

  /** 相对后端根地址的路径，如 "song/detail?id=1"。 */
  url: string;

  /** 请求体，仅 post / put 生效。 */
  data?: object;

  /** 透传给 axios 的额外配置，如 params、headers。 */
  config?: AxiosRequestConfig;
}

/** 发起请求并直接解包为后端 R 结构。GET / */
export function api<T = unknown>(options: ApiRequestOptions): Promise<ApiResponse<T>> {
  const method = (options.method ?? "get").toLowerCase() as HttpMethod;

  const { url, data = {}, config } = options;

  const mapBody = (response: { data: unknown }) => response.data as ApiResponse<T>;

  switch (method) {
    case "post":
      return request.post(url, data, config).then(mapBody);

    case "delete":
      return request.delete(url, config).then(mapBody);

    case "put":
      return request.put(url, data, config).then(mapBody);

    case "get":
    default:
      return request.get(url, config).then(mapBody);
  }
}

/** 以 blob 形式取文件流，用于音频下载 */
export function getBlob(url: string, config?: AxiosRequestConfig): Promise<Blob> {
  return request
    .get(url, { ...config, responseType: "blob" })
    .then((response) => response.data as Blob);
}
