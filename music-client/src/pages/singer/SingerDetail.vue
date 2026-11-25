<template>
  <div class="singer-detail-container">

    <div class="mobile-header">
      <div class="mobile-cover">
        <el-image class="mobile-singer-img" fit="cover" :src="attachImageUrl(songDetails.pic)">
          <template #error>
            <img class="mobile-singer-img" :src="attachImageUrl('/img/singerPic/tubiao.jpg')" />
          </template>
        </el-image>
        <div class="mobile-overlay"></div>
        <div class="mobile-info">
          <h1>{{ songDetails.name }}</h1>
          <p class="mobile-intro">{{ songDetails.introduction }}</p>
        </div>
      </div>
      <div class="mobile-detail">
        <div class="detail-item">
          <span class="detail-label">性别</span>
          <span class="detail-value">{{ getUserSex(songDetails.sex) }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">生日</span>
          <span class="detail-value">{{ getBirth(songDetails.birth) }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">故乡</span>
          <span class="detail-value">{{ songDetails.location }}</span>
        </div>
      </div>
    </div>

    <el-container class="desktop-layout">
      <el-aside class="album-slide">
        <el-image class="singer-img" fit="contain" :src="attachImageUrl(songDetails.pic)">
          <template #error>
            <img class="singer-img" :src="attachImageUrl('/img/singerPic/tubiao.jpg')" />
          </template>
        </el-image>
        <div class="album-info">
          <h2>基本资料</h2>
          <ul>
            <li v-if="songDetails.sex !== 2">性别：{{ getUserSex(songDetails.sex) }}</li>
            <li>生日：{{ getBirth(songDetails.birth) }}</li>
            <li>故乡：{{ songDetails.location }}</li>
          </ul>
        </div>
      </el-aside>

      <el-main class="album-main">
        <h1>{{ songDetails.name }}</h1>
        <p>{{ songDetails.introduction }}</p>
        <song-list :songList="currentSongList"></song-list>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from "vue";
import SongList from "@/components/business/SongList.vue";
import { attachImageUrl, getBirth, getUserSex } from "@/utils";
import { fetchSongListBySingerId } from "@/api/song";
import { useSongStore } from "@/store/song";

const songStore = useSongStore();

const currentSongList = ref<any[]>([]);

const songDetails = computed(() => songStore.songDetails || {}) as any;

async function loadSingerDetailSongs(singerId: string | number | undefined) {
  if (!singerId) {
    currentSongList.value = [];
    return;
  }
  currentSongList.value = await fetchSongListBySingerId(singerId);
}

watch(
  () => songDetails.value.id,
  async (id) => {
    try {
      await loadSingerDetailSongs(id);
    } catch (error) {
      console.error("[SingerDetail] loadSingerDetailSongs failed:", error);
    }
  },
  { immediate: true },
);
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.singer-detail-container {
  min-height: 100%;
  padding-bottom: 80px;
}

.mobile-header { display: none; }
.desktop-layout { display: flex; }

.album-slide {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 20px;
  .singer-img { height: 250px; width: 250px; border-radius: 10%; }
  .album-info { width: 60%; padding-top: 2rem; li { width: 100%; height: 30px; line-height: 30px; } }
}

.album-main {
  p { color: rgba(0, 0, 0, 0.5); margin: 10px 0 20px 0px; }
}

@media screen and (min-width: $md) {
  .album-slide { position: fixed; width: 320px; }
  .album-main { min-width: 500px; padding-right: 4%; margin-left: 340px; }
}

@media screen and (max-width: $md) and (min-width: $sm) {
  .album-slide { width: 280px; position: fixed; }
  .album-slide .singer-img { height: 200px; width: 200px; }
  .album-main { margin-left: 300px; padding-right: 2%; }
}

@media screen and (max-width: $sm) {
  .desktop-layout { display: none; }
  .mobile-header { display: block; padding-bottom: 16px; }
  .mobile-cover { position: relative; height: 200px; overflow: hidden; }
  .mobile-singer-img { width: 100%; height: 100%; object-fit: cover; }
  .mobile-overlay {
    position: absolute; top: 0; left: 0; right: 0; bottom: 0;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  }
  .mobile-info { position: absolute; bottom: 16px; left: 16px; right: 16px; color: #fff; }
  .mobile-info h1 { font-size: 24px; font-weight: 600; margin: 0 0 8px 0; text-shadow: 0 1px 2px rgba(0,0,0,0.5); }
  .mobile-intro { font-size: 13px; color: rgba(255,255,255,0.8); margin: 0; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; }
  .mobile-detail { background: #fff; padding: 16px; margin: 0 12px; border-radius: 12px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
  .detail-item { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid #f0f0f0; &:last-child { border-bottom: none; } }
  .detail-label { font-size: 14px; color: #999; }
  .detail-value { font-size: 14px; color: #333; font-weight: 500; }
  .album-main { margin: 0; padding: 0 12px; }
  .album-main h1, .album-main p { display: none; }
}

@media screen and (max-width: $xs) {
  .mobile-cover { height: 180px; }
  .mobile-info h1 { font-size: 20px; }
  .mobile-detail { padding: 12px; margin: 0 8px; }
}
</style>
