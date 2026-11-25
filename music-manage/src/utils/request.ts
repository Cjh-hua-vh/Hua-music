import axios from "axios";
import type { ApiResponse } from "@/api/types";
import router from "../router";

/** 后端地址，由 vue.config.js 在编译期注入 NODE_HOST 得到。 */
const BASE_URL = process.env.NODE_HOST;

/** 超时给到 30s，因为管理端要传音频、封面这类大文件。 */
axios.defaults.timeout = 30000;

/** 后端用 HttpSession 保存管理员登录态，跨端口请求必须带 Cookie。 */
axios.defaults.withCredentials = true;

axios.defaults.baseURL = BASE_URL;

axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

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
axios.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers = config.headers ?? {};
    (config.headers as Record<string, string>).Authorization = `Bearer ${token}`;
  }
  return config;
});

/** 响应拦截器：HTTP 层不成功统一转 reject */
axios.interceptors.response.use(
  (response) => {
    if (response.status === 200) {
      return Promise.resolve(response);
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

/** GET 请求，直接把 response.data（后端 R）抛给上层。 */
function rawGet(url: string, config?: object) {
  return new Promise((resolve, reject) => {
    axios.get(url, config).then(
      (response) => resolve(response.data),
      (error) => reject(error),
    );
  });
}

/** POST 请求，data 默认按 JSON 提交。 */
function rawPost(url: string, data: object = {}) {
  return new Promise((resolve, reject) => {
    axios.post(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error),
    );
  });
}

/** DELETE 请求；axios.delete 第二个参数是 config 而非 body，参数需拼在 url 上 */
function rawDelete(url: string, data: object = {}) {
  return new Promise((resolve, reject) => {
    axios.delete(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error),
    );
  });
}

/** PUT 请求。 */
function rawPut(url: string, data: object = {}) {
  return new Promise((resolve, reject) => {
    axios.put(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error),
    );
  });
}

export type HttpMethod = "get" | "post" | "delete" | "put";

export interface ApiRequestOptions {
  /** 请求方法，默认 get。 */
  method?: HttpMethod;
  /** 相对后端根地址的路径，如 "song/page?page=1"。 */
  url: string;
  /** 请求体，仅 post / put 生效。 */
  data?: object;
  /** 透传给 axios 的额外配置，仅 get 生效。 */
  config?: object;
}

/** 统一请求入口，返回后端 R 结构而非 axios 响应 */
export function api<T = unknown>(options: ApiRequestOptions): Promise<ApiResponse<T>> {
  const method = (options.method ?? "get").toLowerCase() as HttpMethod;
  const { url, data = {}, config } = options;
  let raw: Promise<unknown>;
  switch (method) {
    case "post":
      raw = rawPost(url, data);
      break;
    case "delete":
      raw = rawDelete(url, data);
      break;
    case "put":
      raw = rawPut(url, data);
      break;
    case "get":
    default:
      raw = rawGet(url, config);
      break;
  }
  return raw as Promise<ApiResponse<T>>;
}
