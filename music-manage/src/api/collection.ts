import { api } from "@/utils/request";
import { routeQueryValue } from "@/utils/routeQuery";

/** 查询某用户收藏的歌曲。GET collection/detail?userId=:userId */
export function getCollectionOfUser(userId: unknown) {
  return api({ url: `collection/detail?userId=${routeQueryValue(userId)}` });
}

/** 取消收藏。DELETE collection/delete?userId=:userId&songId=:songId */
export function deleteCollection(userId: unknown, songId: string | number) {
  return api({
    method: "delete",
    url: `collection/delete?userId=${routeQueryValue(userId)}&songId=${songId}`,
  });
}
