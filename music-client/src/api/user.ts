import { api, getBaseURL, type ApiResponse } from "@/utils/request";
import { jsonRequestConfig } from "@/api/types";

/** 查询用户详情。GET user/detail?id=:id；注意：不吞异常 */
export async function fetchUserById(id: string | number): Promise<ApiResponse> {
  return await api({ url: `user/detail?id=${id}` });
}

/** 查询用户收藏的歌曲。GET collection/detail?userId=:userId */
export async function fetchCollectionByUserId(userId: string | number): Promise<ApiResponse> {
  return await api({ url: `collection/detail?userId=${userId}` });
}

/** 取消收藏歌曲。DELETE collection/delete?userId=:userId&songId=:songId */
export async function fetchDeleteCollection(
  userId: string | number,
  songId: string | number,
): Promise<ApiResponse> {
  return await api({
    method: "delete",
    url: `collection/delete?userId=${userId}&songId=${songId}`,
  });
}

/** 收藏歌曲。POST collection/add */
export async function fetchSetCollection(payload: {
  userId: string | number;
  type: string;
  songId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "collection/add",
    data: payload,
    config: jsonRequestConfig,
  });
}

/** 查询歌曲收藏状态，用于播放页爱心按钮的初始态。POST collection/status */
export async function fetchCollectionStatus(payload: {
  userId: string | number;
  type: string;
  songId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "collection/status",
    data: payload,
    config: jsonRequestConfig,
  });
}

/** 取消收藏歌单。DELETE collection/songList/delete?userId=:userId&songListId=:songListId */
export async function fetchDeleteSongListCollection(
  userId: string | number,
  songListId: string | number,
): Promise<ApiResponse> {
  return await api({
    method: "delete",
    url: `collection/songList/delete?userId=${userId}&songListId=${songListId}`,
  });
}

/** 查询歌单收藏状态。POST collection/songList/status */
export async function fetchSongListCollectionStatus(payload: {
  userId: string | number;
  songListId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "collection/songList/status",
    data: payload,
    config: jsonRequestConfig,
  });
}

/** 收藏歌单。POST collection/songList/add */
export async function fetchSetSongListCollection(payload: {
  userId: string | number;
  songListId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "collection/songList/add",
    data: payload,
    config: jsonRequestConfig,
  });
}

/** 查询收藏了指定歌单的用户。GET collection/songList/collectors?songListId=:songListId */
export async function fetchSongListCollectors(songListId: string | number): Promise<ApiResponse> {
  return await api({ url: `collection/songList/collectors?songListId=${songListId}` });
}

/** 删除用户（后台用户管理）。GET user/delete?id=:id */
export async function fetchDeleteUser(id: string | number): Promise<ApiResponse> {
  return await api({ url: `user/delete?id=${id}` });
}

/** 修改用户资料，不含密码与头像。POST user/update */
export async function fetchUpdateUserMsg(payload: {
  id: string | number;
  username: string;
  sex: string | number;
  phoneNum: string;
  email: string;
  birth: string | Date;
  introduction: string;
  location: string;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "user/update",
    data: payload,
    config: jsonRequestConfig,
  });
}

/** 修改密码，需携带旧密码由后端校验。POST user/updatePassword */
export async function fetchUpdateUserPassword(payload: {
  id: string | number;
  username: string;
  oldPassword: string;
  password: string;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "user/updatePassword",
    data: payload,
    config: jsonRequestConfig,
  });
}

/** 生成用户头像的上传地址，供 el-upload 的 action 使用 */
export function fetchUploadUrl(userId: string | number): string {
  return `${getBaseURL()}/user/avatar/update?id=${userId}`;
}
