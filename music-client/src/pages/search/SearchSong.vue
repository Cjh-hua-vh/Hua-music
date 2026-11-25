<template>
  <div class="search-song">
    <SongList v-if="currentSongList.length" :songList="currentSongList"></SongList>
    <el-empty v-else description="暂无相关歌曲" />
  </div>
</template>

<script lang="ts">
import { ref } from "vue";

import SongList from "../../components/business/SongList.vue";
import { fetchSongBySingerName } from "@/api/song";
import { useSearchKeyword } from "../../composables/useSearchKeyword";

type SearchSongItem = {
  id: string | number;
  name: string;
  url: string;
  pic: string;
  lyric?: string;
  introduction?: string;
};

export default {
  name: "SearchSong",
  components: {
    SongList,
  },
  setup() {
    const currentSongList = ref<SearchSongItem[]>([]);

    async function searchSong(keywords: string) {
      if (keywords === "") {
        currentSongList.value = [];
        return;
      }
      const result = await fetchSongBySingerName(keywords);
      const list = Array.isArray(result && result.data) ? result.data as any[] : [];
      if (!list.length) {
        currentSongList.value = [];
      } else {
        currentSongList.value = list;
      }
    }

    useSearchKeyword(searchSong);

    return {
      currentSongList,
    };
  },
};
</script>