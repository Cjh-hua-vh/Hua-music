<template>
  <div id="app">
    <keep-alive><router-view v-slot="{ Component }">
      <component v-if="isLoginRoute" :is="Component" />
      <hua-layout v-else-if="Component">
        <component :is="Component" />
      </hua-layout>
    </router-view></keep-alive>

  </div>
</template>

<script lang="ts" setup>

import { computed } from "vue";
import { useRoute } from "vue-router";
import HuaLayout from "@/components/layouts/HuaLayout.vue";
import { useAppStore } from "@/stores/app";

const route = useRoute();

const isLoginRoute = computed(() => route.path === "/");

const appStore = useAppStore();

const raw = sessionStorage.getItem("dataStore");
if (raw) {
  try {
    appStore.$patch(JSON.parse(raw));
  } catch {
    // 缓存内容损坏，忽略
  }
}

window.addEventListener("beforeunload", () => {
  sessionStorage.setItem("dataStore", JSON.stringify(appStore.$state));
});
</script>
