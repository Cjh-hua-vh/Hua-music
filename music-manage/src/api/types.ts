/** 后端统一响应体 R 的前端映射 */
export interface ApiResponse<T = unknown> {
  code: number | string;
  message: string;
  success: boolean;
  data?: T;
  type?: string;
}
