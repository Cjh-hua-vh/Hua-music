<template>
  <el-breadcrumb class="crumbs" separator="/">
    <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.name" :to="{ path: item.path, query: item.query }">
      {{ item.name }}
    </el-breadcrumb-item>
  </el-breadcrumb>

  <div class="container">
    <div class="handle-box">
      <el-button @click="deleteAll">批量删除</el-button>
      <el-input v-model="searchWord" placeholder="筛选关键词"></el-input>
      <el-button type="primary" @click="centerDialogVisible = true">添加歌曲</el-button>
    </div>
    <el-table height="550px" border size="small" :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" align="center"></el-table-column>
      <el-table-column label="ID" prop="id" width="50" align="center"></el-table-column>
      <el-table-column label="歌手-歌曲" prop="name"></el-table-column>
      <el-table-column label="操作" width="90" align="center">
        <template v-slot="scope">
          <el-button type="danger" @click="deleteRow(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <el-dialog title="添加歌曲" v-model="centerDialogVisible">
    <el-form label-width="80px" :model="registerForm">
      <el-form-item prop="singerName" label="歌手名字">
        <el-input v-model="registerForm.singerName"></el-input>
      </el-form-item>
      <el-form-item prop="songName" label="歌曲名字">
        <el-input v-model="registerForm.songName"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveSong">确 定</el-button>
      </span>
    </template>
  </el-dialog>

  <hua-del-dialog :delVisible="delVisible" @confirm="confirm" @cancelRow="delVisible = $event"></hua-del-dialog>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, watch, ref, reactive, computed } from "vue";
import { useRoute } from "vue-router";
import { useAppStore } from "@/stores/app";
import {
  getListSongOfSongId,
  getSongOfId,
  getSongOfSingerName,
  setListSong,
  deleteListSong,
} from "@/api";
import HuaDelDialog from "@/components/dialog/HuaDelDialog.vue";

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
      const result = (await getListSongOfSongId(route.query.id)) as ResponseBody;
      for (let item of result.data) {
        const result = await getSongOfId(item.songId) as ResponseBody;
        if (result && result.data) {
          tableData.value.push(result.data);
          tempDate.value.push(result.data);
        }
      }
    }

    const centerDialogVisible = ref(false);
    const registerForm = reactive({
      singerName: "",
      songName: "",
    });

    async function saveSong() {
      const id = `${registerForm.singerName}-${registerForm.songName}`;
      const result = (await getSongOfSingerName(id)) as ResponseBody;

      if (result.success) {
        addSong(result.data[0].id);
      }
    }
    async function addSong(id) {
      let songId = id;
      let songListId = route.query.id as string;

      const result = (await setListSong({ songId, songListId })) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.success) getData();
      centerDialogVisible.value = false;
    }

    const idx = ref(-1);
    const multipleSelection = ref([]);
    const delVisible = ref(false);

    async function confirm() {
      const result = await deleteListSong(idx.value) as ResponseBody;
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
      centerDialogVisible,
      registerForm,
      breadcrumbList,
      deleteAll,
      handleSelectionChange,
      deleteRow,
      confirm,
      saveSong,
    };
  },
});
</script>

<style scoped></style>
