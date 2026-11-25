/** 管理端 API 层统一出口，页面组件一律从 "@/api" 引入 */
export { api, getBaseURL } from "@/utils/request";
export type { ApiRequestOptions, HttpMethod } from "@/utils/request";

export * from "./types";
export * from "./url";
export * from "./admin";
export * from "./user";
export * from "./collection";
export * from "./comment";
export * from "./banner";
export * from "./singer";
export * from "./song";
export * from "./songSheet";
export * from "./listSong";
