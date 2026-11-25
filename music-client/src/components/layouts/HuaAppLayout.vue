<template>
  <div class="hua-layout">

    <header class="hua-layout-header">
      <HuaHeader />
    </header>

    <div class="hua-layout-content">
      <div class="main-content" style="position: relative;">

        <router-view v-slot="{ Component }">
          <keep-alive :include="cachedComponents">
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </div>

      <HuaPlayQueue />
      <HuaPlayerBar />
      <SongCommentDialog
        :model-value="showSongCommentDialog"
        :song-id="songId"
        @update:modelValue="configureStore.setShowSongCommentDialog"
      />
      <el-backtop :right="100" :bottom="100" />
    </div>

    <HuaFooter v-if="!isLyricPage" />
  </div>
</template>

<script lang="ts" setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import HuaHeader from "./HuaHeader.vue";
import HuaPlayQueue from "./HuaPlayQueue.vue";
import HuaPlayerBar from "./HuaPlayerBar.vue";
import HuaFooter from "./HuaFooter.vue";
import SongCommentDialog from "../business/SongCommentDialog.vue";
import { useConfigureStore } from "../../store/configure";
import { useUserStore } from "../../store/user";
import { useSongStore } from "../../store/song";
import { usePlayerStore } from "../../store/player";
import { RouterName } from "../../enums";

const configureStore = useConfigureStore();
const userStore = useUserStore();
const songStore = useSongStore();
const playerStore = usePlayerStore();
const route = useRoute();
const STORE_CACHE_KEY = "dataStore";
const songId = computed(() => songStore.songId);
const showSongCommentDialog = computed(() => configureStore.showSongCommentDialog);
const isLyricPage = computed(() => route.path.startsWith(`${RouterName.Lyric}`));

const cachedComponents = ["Home", "Singer", "SongSheet", "Search"];

if (sessionStorage.getItem(STORE_CACHE_KEY)) {
  const cacheStore = JSON.parse(sessionStorage.getItem(STORE_CACHE_KEY) as string);
  if (cacheStore.configure) configureStore.$patch(cacheStore.configure);
  if (cacheStore.user) userStore.$patch(cacheStore.user);
  if (cacheStore.song) songStore.$patch(cacheStore.song);
  if (cacheStore.player) playerStore.$patch(cacheStore.player);
}

const saveStoreToSession = () => {
  sessionStorage.setItem(
    STORE_CACHE_KEY,
    JSON.stringify({
      configure: configureStore.$state,
      user: userStore.$state,
      song: songStore.$state,
      player: playerStore.$state,
    })
  );
};
window.addEventListener("beforeunload", saveStoreToSession);
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";
.hua-layout { min-height: calc(100vh - 60px); }
.hua-layout-header { padding: 0; }
.hua-layout-content {
  display: flex; flex-direction: column; flex: 1; min-height: 0; padding: 0; margin: 0;
}
.main-content { flex: 1; min-height: 0; padding-top: 56px; }
</style>
