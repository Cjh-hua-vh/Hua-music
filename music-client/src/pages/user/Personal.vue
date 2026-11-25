<template>
  <div class="personal">
    <div class="personal-bg"></div>

    <div class="personal-info">
      <div class="info-container">
        <div class="avatar-wrapper">
          <el-image
            class="personal-img"
            fit="cover"
            :src="attachImageUrl(userPic)"
            @click="dialogTableVisible = true"
          />
          <div class="avatar-upload-icon" @click="dialogTableVisible = true">
            <Edit class="icon" />
          </div>
        </div>
        <div class="personal-msg">
          <div class="username">{{ personalInfo.username }}</div>
          <div class="introduction">{{ personalInfo.introduction || "这个人很懒，什么都没写" }}</div>
        </div>
      </div>
      <el-button class="edit-info" round :icon="Edit" @click="goPage">修改个人信息</el-button>
    </div>

    <div class="personal-body">
      <el-tabs v-model="activeTab" class="personal-tabs">
        <el-tab-pane :label="`收藏歌曲 (${collectSongList.length})`" name="songs">
          <SongList :songList="collectSongList" :show="true" @changeData="changeData"></SongList>
        </el-tab-pane>
        <el-tab-pane :label="`收藏歌单 (${collectSongSheetList.length})`" name="songSheets">
          <CoverCardGrid :playList="collectSongSheetList" path="song-sheet-detail"></CoverCardGrid>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog v-model="dialogTableVisible" title="修改头像">
      <upload></upload>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from "vue";
import { Edit } from "@element-plus/icons-vue";

import SongList from "@/components/business/SongList.vue";
import CoverCardGrid from "@/components/business/CoverCardGrid.vue";
import Upload from "@/pages/user/Upload.vue";
import { useUserStore } from "@/store/user";
import { fetchCollectionByUserId, fetchUserById } from "@/api/user";
import { RouterName } from "@/enums";
import { attachImageUrl } from "@/utils";
import { ensureArray } from "@/utils/api-response-data";
import { useAppActions } from "@/composables/useAppActions";

type UserCollectionDetail = {
  songs?: any[];
  songSheets?: any[];
};

const userStore = useUserStore();
const { routerManager } = useAppActions();

const dialogTableVisible = ref(false);
const activeTab = ref("songs");
const collectSongList = ref<any[]>([]);
const collectSongSheetList = ref<any[]>([]);
const personalInfo = reactive({
  username: "",
  userSex: "",
  birth: "",
  location: "",
  introduction: "",
});

const userId = computed(() => userStore.userId);
const userPic = computed(() => userStore.userPic);

watch(userPic, () => {
  dialogTableVisible.value = false;
});

function goPage() {
  routerManager(RouterName.Setting, { path: RouterName.Setting });
}

async function getUserInfo(id: string | number) {
  const result = await fetchUserById(id);
  const user = result && result.data ? result.data : null;
  if (!user) return;

  personalInfo.username = user.username || "";
  personalInfo.userSex = user.sex || "";
  personalInfo.birth = user.birth || "";
  personalInfo.introduction = user.introduction || "";
  personalInfo.location = user.location || "";
}

async function getCollection(id: string | number) {
  collectSongList.value = [];
  collectSongSheetList.value = [];
  const result = await fetchCollectionByUserId(id);
  const raw = result != null ? result.data : undefined;
  if (!raw || typeof raw !== "object" || Array.isArray(raw)) return;
  const data = raw as UserCollectionDetail;
  collectSongList.value = ensureArray(data.songs);
  collectSongSheetList.value = ensureArray(data.songSheets);
}

function changeData() {
  getCollection(userId.value);
}

onMounted(() => {
  getUserInfo(userId.value);
  getCollection(userId.value);
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.personal {
  position: relative;
  min-height: 100vh;
  padding-top: 200px;

  .personal-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 200px;
    background: linear-gradient(135deg, $color-blue-shallow 0%, $color-blue 100%);
  }
}

.personal-info {
  position: relative;
  padding: 0 16px;
  margin-bottom: 24px;
}

.info-container {
  display: flex;
  align-items: flex-end;
  gap: 16px;
}

.avatar-wrapper {
  position: relative;
}

.personal-img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 4px solid $color-white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  cursor: pointer;
}

.avatar-upload-icon {
  position: absolute;
  bottom: 4px;
  right: 4px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  opacity: 0;
  transition: opacity 0.3s;

  .icon {
    width: 16px;
    height: 16px;
    color: #fff;
  }
}

.avatar-wrapper:hover .avatar-upload-icon,
.avatar-wrapper:active .avatar-upload-icon {
  opacity: 1;
}

.personal-msg {
  flex: 1;
  padding-bottom: 8px;

  .username {
    font-size: 24px;
    font-weight: 700;
    color: $color-black;
    margin-bottom: 4px;
  }

  .introduction {
    font-size: 14px;
    color: rgba(0, 0, 0, 0.55);
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.edit-info {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
}

.personal-body {
  padding: 0 16px;
}

.personal-tabs {
  background: #fff;
  border-radius: 12px;
  padding: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

@media screen and (min-width: $md) {
  .personal {
    padding-top: 220px;

    .personal-bg {
      height: 220px;
    }
  }

  .personal-info {
    padding: 0 8%;
    margin-bottom: 40px;
  }

  .avatar-wrapper {
    margin-left: 20px;
  }

  .personal-img {
    width: 180px;
    height: 180px;
    margin-top: -80px;
  }

  .personal-msg {
    padding-bottom: 20px;

    .username {
      font-size: 36px;
      margin-bottom: 8px;
    }

    .introduction {
      font-size: 18px;
    }
  }

  .edit-info {
    right: 8%;
    top: 40px;
    transform: none;
  }

  .personal-body {
    padding: 0 8%;
  }

  .personal-tabs {
    padding: 16px;
  }
}

@media screen and (max-width: $md) and (min-width: $sm) {
  .personal {
    padding-top: 210px;

    .personal-bg {
      height: 210px;
    }
  }

  .personal-info {
    padding: 0 4%;
  }

  .personal-img {
    width: 150px;
    height: 150px;
  }

  .personal-msg .username {
    font-size: 28px;
  }

  .personal-body {
    padding: 0 4%;
  }
}

@media screen and (max-width: $sm) {
  .avatar-upload-icon {
    opacity: 1;
  }

  .edit-info {
    position: static;
    transform: none;
    margin-top: 16px;
    width: 100%;
  }

  .info-container {
    flex-direction: column;
    align-items: flex-start;
  }

  .personal-msg {
    padding-bottom: 0;
  }
}

@media screen and (max-width: $xs) {
  .personal-img {
    width: 100px;
    height: 100px;
  }

  .personal-msg .username {
    font-size: 20px;
  }

  .personal-msg .introduction {
    font-size: 13px;
  }
}
</style>
