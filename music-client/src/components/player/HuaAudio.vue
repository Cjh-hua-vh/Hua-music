<template>
  <audio
    :src="attachImageUrl(songUrl)"
    controls
    ref="audioRef"
    preload="true"
    @canplay="canplay"
    @timeupdate="timeupdate"
    @ended="ended"
  ></audio>
</template>

<script lang="ts" setup>
import { ref, computed, watch, onMounted } from "vue";
import { PLAY_MODE_SINGLE_LOOP, useSongStore } from "../../store/song";
import { attachImageUrl } from "../../utils";

const songStore = useSongStore();
const audioRef = ref<HTMLAudioElement | null>(null);
const pendingUserGesturePlay = ref(false);

const playMode = computed(() => songStore.playMode);
const songUrl = computed(() => songStore.songUrl);
const isPlay = computed(() => songStore.isPlay);
const volume = computed(() => songStore.volume);
const changeTime = computed(() => songStore.changeTime);
const autoNext = computed(() => songStore.autoNext);

watch(isPlay, () => togglePlay());

watch(changeTime, () => {
  if (audioRef.value) {
    audioRef.value.currentTime = changeTime.value;
  }
});

watch(volume, (value) => {
  if (audioRef.value) {
    audioRef.value.volume = value;
  }
});

watch(playMode, syncAudioLoop, { immediate: true });

async function tryPlay() {
  if (!audioRef.value) return;
  try {
    await audioRef.value.play();
    pendingUserGesturePlay.value = false;
    songStore.setIsPlay(true);
  } catch {
    pendingUserGesturePlay.value = true;
    songStore.setIsPlay(false);
  }
}

function togglePlay() {
  if (!audioRef.value) return;
  if (isPlay.value) {
    tryPlay();
  } else {
    audioRef.value.pause();
  }
}

function canplay() {
  if (!audioRef.value) return;
  syncAudioLoop();
  songStore.setDuration(audioRef.value.duration);
  tryPlay();
}

function timeupdate() {
  if (!audioRef.value) return;
  songStore.setCurTime(audioRef.value.currentTime);
}

function ended() {
  if (playMode.value === PLAY_MODE_SINGLE_LOOP) return;
  songStore.setIsPlay(false);
  songStore.setCurTime(0);
  songStore.setAutoNext(!autoNext.value);
}

function syncAudioLoop() {
  if (!audioRef.value) return;
  audioRef.value.loop = playMode.value === PLAY_MODE_SINGLE_LOOP;
}

const retryAfterGesture = () => {
  if (pendingUserGesturePlay.value && audioRef.value && audioRef.value.src) {
    tryPlay();
  }
};

onMounted(() => {
  window.addEventListener("pointerdown", retryAfterGesture, { passive: true });
  window.addEventListener("keydown", retryAfterGesture);
  window.addEventListener("touchstart", retryAfterGesture, { passive: true });
});
</script>

<style scoped>
audio {
  display: none;
}
</style>
