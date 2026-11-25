import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

const routes: Array<RouteRecordRaw> = [

  {
    path: "/:pathMatch(.*)*",
    redirect: "/404",
  },

  {
    path: "/404",
    component: () => import("@/pages/error/404.vue"),
  },

  {
    path: "/",
    name: "hua-app-layout",
    component: () => import("@/components/layouts/HuaAppLayout.vue"),
    children: [

      {
        path: "/",
        name: "home",
        meta: {
          keepAlive: true,
        },
        component: () => import("@/pages/home/Home.vue"),
      },

      {
        path: "/sign-in",
        name: "sign-in",
        component: () => import("@/pages/auth/SignIn.vue"),
      },

      {
        path: "/sign-up",
        name: "sign-up",
        component: () => import("@/pages/auth/SignUp.vue"),
      },

      {
        path: "/forgot-password",
        name: "forgot-password",
        component: () => import("@/pages/auth/ForgotPassword.vue"),
      },

      {
        path: "/personal",
        name: "personal",
        meta: {
          requireAuth: true,
        },
        component: () => import("@/pages/user/Personal.vue"),
      },

      {
        path: "/song-sheet",
        name: "song-sheet",
        meta: {
          keepAlive: true,
        },
        component: () => import("@/pages/song-sheet/SongSheet.vue"),
      },

      {
        path: "/song-sheet-detail/:id",
        name: "song-sheet-detail",
        component: () => import("@/pages/song-sheet/SongSheetDetail.vue"),
      },

      {
        path: "/singer",
        name: "singer",
        meta: {
          keepAlive: true,
        },
        component: () => import("@/pages/singer/Singer.vue"),
      },

      {
        path: "/singer-detail/:id",
        name: "singer-detail",
        component: () => import("@/pages/singer/SingerDetail.vue"),
      },

      {
        path: "/lyric/:id",
        name: "lyric",
        component: () => import("@/pages/lyric/Lyric.vue"),
      },

      {
        path: "/search",
        name: "search",
        meta: {
          keepAlive: true,
        },
        component: () => import("@/pages/search/Search.vue"),
      },

      {
        path: "/personal-data",
        name: "personal-data",
        component: () => import("@/pages/user/PersonalData.vue"),
      },

      {
        path: "/setting",
        name: "setting",
        meta: {
          requireAuth: true,
        },
        component: () => import("@/pages/user/Setting.vue"),
        children: [
          {
            path: "/setting/PersonalData",
            name: "personalData",
            meta: {
              requireAuth: true,
            },
            component: () => import("@/pages/user/PersonalData.vue"),
          }
        ]
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,

  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    }
    if (to.meta.keepAlive) {
      const scrollPositions = JSON.parse(sessionStorage.getItem("scrollPositions") || "{}");
      if (scrollPositions[to.fullPath]) {
        return scrollPositions[to.fullPath];
      }
    }
    return { top: 0 };
  },
});

router.beforeEach((to, from, next) => {
  if (from.meta.keepAlive) {
    const scrollY = window.scrollY;
    const scrollPositions = JSON.parse(sessionStorage.getItem("scrollPositions") || "{}");
    scrollPositions[from.fullPath] = { top: scrollY };
    sessionStorage.setItem("scrollPositions", JSON.stringify(scrollPositions));
  }
  next();
});

export default router;
