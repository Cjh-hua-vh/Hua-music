<template>
  <div class="home-container">

    <el-carousel
      v-if="swiperList.length"
      class="swiper-container"
      type="card"
      :height="carouselHeight"
      :interval="4000"
      :autoplay="true"
    >
      <el-carousel-item v-for="(item, index) in swiperList" :key="index">
        <img :src="attachImageUrl(item.pic)" @click="handleBannerClick(item.url)" />
      </el-carousel-item>
    </el-carousel>

    <CoverCardGrid
      class="play-list-container"
      title="热门歌单"
      path="song-sheet-detail"
      :playList="songList"
    />
  </div>
</template>

<script lang="ts">
export default {
  name: "Home",
};
</script>

<script lang="ts" setup>
import { ref, computed } from "vue";
import CoverCardGrid from "@/components/business/CoverCardGrid.vue";
import { fetchBannerList } from "@/api/banner";
import { fetchRecommendedSongList, fetchSongList } from "@/api/songSheet";
import { attachImageUrl } from "@/utils";

const songList = ref([]);
const swiperList = ref([]);

const carouselHeight = computed(() => {
  if (typeof window !== 'undefined') {
    const width = window.innerWidth;
    if (width < 576) {
      return '28vw';
    } else if (width < 768) {
      return '24vw';
    }
  }
  return '20vw';
});

async function loadHomeData() {
  try {
    const [banners, recommendedSongs] = await Promise.all([
      fetchBannerList(),
      fetchRecommendedSongList(10),
    ]);

    swiperList.value = banners;

    songList.value = recommendedSongs.length ? recommendedSongs : await fetchSongList(10);
  } catch (error) {
    console.error("[Home] loadHomeData failed:", error);
  }
}

loadHomeData();

function handleBannerClick(url?: string) {
  const target = (url || "").trim();
  if (!target) return;

  if (/^https?:\/\//i.test(target)) {
    window.open(target, "_blank");
    return;
  }
  window.location.href = target.startsWith("/") ? target : `/${target}`;
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.home-container {
  min-height: 100%;
  padding-bottom: 120px;
}

.swiper-container {
  width: 90%;
  margin: auto;
  padding-top: 20px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    cursor: pointer;
    border-radius: 8px;
  }

  :deep(.el-carousel__container) {
    border-radius: 8px;
    overflow: hidden;
  }

  :deep(.el-carousel__indicators) {
    bottom: 10px;
  }

  :deep(.el-carousel__indicator) {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.5);
    margin: 0 4px;

    &.is-active {
      background: rgba(255, 255, 255, 0.9);
    }
  }
}

@media screen and (max-width: $sm) {
  .swiper-container {
    width: 94%;
    padding-top: 12px;

    :deep(.el-carousel__indicator) {
      width: 6px;
      height: 6px;
      margin: 0 3px;
    }
  }
}

@media screen and (max-width: $xs) {
  .swiper-container {
    width: 96%;
  }
}

.el-slider__runway {
  background-color: $color-blue;
}
</style>
