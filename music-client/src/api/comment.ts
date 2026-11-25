import { api, type ApiResponse } from "@/utils/request";
import { jsonRequestConfig } from "@/api/types";

/** 查询评论列表：type 决定查的是歌单评论还是歌曲评论：。GET comment/songList/detail?songListId=:id */
export async function fetchAllComment(type: number, id: string | number): Promise<ApiResponse> {
  const url = type === 1
    ? `comment/songList/detail?songListId=${id}`
    : `comment/song/detail?songId=${id}`;
  return await api({ url });
}

/** 发表评论。POST comment/add */
export async function fetchSetComment(payload: {
  userId: string | number;
  content: string;
  songId: string | null;
  songListId: string | null;
  nowType: number | null;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "comment/add",
    data: payload,
    config: jsonRequestConfig,
  });
}

/** 删除评论。GET comment/delete?id=:id */
export async function fetchDeleteComment(id: string | number): Promise<ApiResponse> {
  return await api({ url: `comment/delete?id=${id}` });
}

/** 更新评论的点赞数。POST comment/like */
export async function fetchSetSupport(payload: { id: string | number; likeCount: number }): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "comment/like",
    data: payload,
    config: jsonRequestConfig,
  });
}

/** 查询当前用户是否已给该评论点赞。POST userSupport/test */
export async function fetchTestAlreadySupport(payload: {
  commentId: string | number;
  userId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "userSupport/test",
    data: payload,
    config: jsonRequestConfig,
  });
}

/** 取消点赞，删除 user_support 中的记录。POST userSupport/delete */
export async function fetchDeleteUserSupport(payload: {
  commentId: string | number;
  userId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "userSupport/delete",
    data: payload,
    config: jsonRequestConfig,
  });
}

/** 点赞，写入 user_support 记录。POST userSupport/insert */
export async function fetchInsertUserSupport(payload: {
  commentId: string | number;
  userId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "userSupport/insert",
    data: payload,
    config: jsonRequestConfig,
  });
}
