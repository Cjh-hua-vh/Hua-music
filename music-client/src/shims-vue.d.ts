/* eslint-disable */
declare module "*.vue" {
  import type { DefineComponent } from "vue";
  const component: DefineComponent<{}, {}, any>;
  export default component;
}

declare module "vue/types/vue" {
  import VueRouter, { Route } from "vue-router";

  interface Vue {
    $router: VueRouter;
    $route: Route;
  }
}

declare module "@vue/runtime-core" {
  interface State {
    count: number;
  }

  interface ComponentCustomProperties {
    $store: import("vuex").Store<State>;
  }
}

declare function defineProps<T = any>(props?: T): any;
declare function withDefaults<T, D>(props: T, defaults: D): T;
declare function defineEmits<T = any>(): T;

declare module "vue" {
  export { computed, reactive, ref, toRefs } from "@vue/reactivity";
  export { defineComponent, nextTick, watch, onMounted, getCurrentInstance } from "@vue/runtime-core";
  export { createApp } from "@vue/runtime-dom";
}
