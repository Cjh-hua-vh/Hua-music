import { createApp } from "vue";
import type { Plugin } from "vue";

import { createPinia } from "pinia";

import ElementPlus from "element-plus";

import App from "./App.vue";
import router from "./router";

import "element-plus/dist/index.css";
import "./assets/css/main.css";

const pinia = createPinia();

createApp(App)
  .use(pinia as unknown as Plugin)
  .use(router)
  .use(ElementPlus)
  .mount("#app");

