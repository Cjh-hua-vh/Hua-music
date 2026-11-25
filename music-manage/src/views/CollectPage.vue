<template>
  <el-breadcrumb class="crumbs" separator="/">
    <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.name" :to="{ path: item.path, query: item.query }">
      {{ item.name }}
    </el-breadcrumb-item>
  </el-breadcrumb>

  <div class="container">
    <div class="handle-box">
      <el-button @click="deleteAll">批量删除</el-button>
      <el-input placeholder="筛选歌曲" v-model="searchWord"></el-input>
    </div>
    <el-table height="550px" border size="small" :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" align="center"></el-table-column>
      <el-table-column prop="name" label="歌手-歌曲"></el-table-column>
      <el-table-column label="操作" width="90" align="center">
        <template v-slot="scope">
          <el-button type="danger" @click="deleteRow(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <hua-del-dialog :delVisible="delVisible" @confirm="confirm" @cancelRow="delVisible = $event"></hua-del-dialog>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, watch, ref, computed } from "vue";
import { useRoute } from "vue-router";
import { useAppStore } from "@/stores/app";
import { getCollectionOfUser, deleteCollection } from "@/api";
import HuaDelDialog from "@/components/dialog/HuaDelDialog.vue";

interface ResponseBody {
  code: string;
  success: boolean;
  message: string;
  type: string;
}

export default defineComponent({
  components: {
    HuaDelDialog,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const route = useRoute();
    const appStore = useAppStore();

    const tableData = ref([]);
    const tempDate = ref([]);
    const breadcrumbList = computed(() => appStore.breadcrumbList);

    const searchWord = ref("");
    watch(searchWord, () => {
      if (searchWord.value === "") {
        tableData.value = tempDate.value;
      } else {
        tableData.value = [];
        for (let item of tempDate.value) {
          if (item.name.includes(searchWord.value)) {
            tableData.value.push(item);
          }
        }
      }
    });

    getData();

    async function getData() {
      tableData.value = [];
      tempDate.value = [];
      const result = (await getCollectionOfUser(route.query.id)) as any;
      const songs = result?.data?.songs;
      const list = Array.isArray(songs) ? songs : [];
      tableData.value = list;
      tempDate.value = [...list];
    }

    const idx = ref(-1);
    const multipleSelection = ref([]);
    const delVisible = ref(false);

    async function confirm() {
      const result = (await deleteCollection(route.query.id, idx.value)) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.success) getData();
      delVisible.value = false;
    }
    function deleteRow(id) {
      idx.value = id;
      delVisible.value = true;
    }
    function handleSelectionChange(val) {
      multipleSelection.value = val;
    }
    function deleteAll() {
      for (let item of multipleSelection.value) {
        deleteRow(item.id);
        confirm();
      }
      multipleSelection.value = [];
    }

    return {
      searchWord,
      tableData,
      delVisible,
      breadcrumbList,
      deleteAll,
      handleSelectionChange,
      deleteRow,
      confirm,
    };
  },
});
</script>

<style scoped></style>
