import { defineStore } from "pinia";

export type BreadcrumbItem = {
  name: string;
  path: string;
  query?: unknown;
};

export const useAppStore = defineStore("app", {
  state: () => ({
    userPic: "/img/avatorImages/user.jpg",
    isPlay: false,
    url: "",
    id: "",
    breadcrumbList: [] as BreadcrumbItem[],
  }),

  actions: {
    setUserPic(userPic: string) {
      this.userPic = userPic;
    },
    setIsPlay(isPlay: boolean) {
      this.isPlay = isPlay;
    },
    setUrl(url: string) {
      this.url = url;
    },
    setId(id: string) {
      this.id = id;
    },
    setBreadcrumbList(breadcrumbList: BreadcrumbItem[]) {
      this.breadcrumbList = breadcrumbList;
    },
  },
});
