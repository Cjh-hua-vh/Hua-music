import { api } from "@/utils/request";

/** 查询全部用户。GET user */
export function getAllUser() {
  return api({ url: "user" });
}

/** 查询用户详情。GET user/detail?id=:id */
export function getUserOfId(id: string | number) {
  return api({ url: `user/detail?id=${id}` });
}

/** 删除用户。GET user/delete?id=:id（后端为历史遗留的 GET 写法） */
export function deleteUser(id: string | number) {
  return api({ url: `user/delete?id=${id}` });
}
