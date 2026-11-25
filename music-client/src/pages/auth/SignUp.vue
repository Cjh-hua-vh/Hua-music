<template>
  <AuthLayout title="用户注册" pageClass="sign-up-page">
    <el-form ref="signUpForm" label-width="70px" status-icon :model="registerForm" :rules="SignUpRules">
      <el-form-item prop="avatar">
        <el-upload
          class="avatar-uploader"
          action="#"
          :auto-upload="false"
          :show-file-list="false"
          :on-change="handleAvatarChange"
        >
          <img v-if="avatarUrl" :src="avatarUrl" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item prop="username" label="用户名">
        <el-input v-model="registerForm.username" placeholder="用户名" />
      </el-form-item>
      <el-form-item prop="password" label="密码">
        <el-input type="password" placeholder="密码" v-model="registerForm.password" />
      </el-form-item>
      <el-form-item prop="sex" label="性别">
        <el-radio-group v-model="registerForm.sex">
          <el-radio :label="0">女</el-radio>
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">保密</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="phoneNum" label="手机">
        <el-input placeholder="手机" v-model="registerForm.phoneNum" />
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="registerForm.email" placeholder="邮箱" />
      </el-form-item>
      <el-form-item prop="birth" label="生日">
        <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.birth" style="width: 100%" />
      </el-form-item>
      <el-form-item prop="introduction" label="签名">
        <el-input type="textarea" placeholder="签名" v-model="registerForm.introduction" />
      </el-form-item>
      <el-form-item prop="location" label="地区">
        <el-select v-model="registerForm.location" :options="AREA" placeholder="地区" style="width: 100%" clearable
          filterable />
      </el-form-item>
      <el-form-item class="sign-btn">
        <el-button @click="goBack">返回登录</el-button>
        <el-button type="primary" @click="handleSignUp">确定</el-button>
      </el-form-item>
    </el-form>
  </AuthLayout>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import AuthLayout from "@/components/auth/AuthLayout.vue";
import { fetchSignUpWithAvatar } from "@/api/auth";
import { elMessageTypeFromResponse } from "@/api/types";
import { AREA, RouterName, NavLabel } from "@/enums";
import { useAppActions } from "@/composables/useAppActions";
import { useNavigation } from "@/composables/useNavigation";

const { routerManager } = useAppActions();
const { changeActiveNav } = useNavigation();
const signUpForm = ref<any>(null);
const avatarFile = ref<File | null>(null);
const avatarUrl = ref<string>("");

const registerForm = reactive({
  username: "",
  password: "",
  sex: 2,
  phoneNum: "",
  email: "",
  birth: new Date(),
  introduction: "",
  location: "",
});

function handleAvatarChange(file: any) {
  const rawFile = file.raw;
  if (!rawFile) return;
  if (!rawFile.type.startsWith("image/")) {
    ElMessage.error("请上传图片文件");
    return;
  }
  avatarFile.value = rawFile;
  avatarUrl.value = URL.createObjectURL(rawFile);
}

const SignUpRules = {
  username: [{ required: true, trigger: "blur", min: 3 }],
  password: [{ required: true, trigger: "blur", min: 3 }],
  sex: [{ required: true, message: "请选择性别", trigger: "change" }],
  phoneNum: [{ message: "请输入手机号码", trigger: "blur" }],
  email: [
    { message: "请输入邮箱地址", trigger: "blur" },
    {
      type: "email",
      message: "请输入正确的邮箱地址",
      trigger: ["blur", "change"],
    },
  ],
  birth: [
    { required: true, type: "date", message: "请选择日期", trigger: "change" },
  ],
  introduction: [{ message: "请输入介绍", trigger: "blur" }],
  location: [{ message: "请输入地区", trigger: "change" }],
};

function formatBirthForBackend(value: Date): string {
  const year = value.getFullYear();
  const month = `${value.getMonth() + 1}`.padStart(2, "0");
  const day = `${value.getDate()}`.padStart(2, "0");
  return `${year}-${month}-${day} 00:00:00`;
}

async function handleSignUp() {
  if (!signUpForm.value) return;

  if (!avatarFile.value) {
    ElMessage.error("请上传头像图片");
    return;
  }

  try {
    await signUpForm.value.validate();
  } catch {
    return;
  }

  const formData = new FormData();
  formData.append("avatar", avatarFile.value);
  formData.append("username", registerForm.username);
  formData.append("password", registerForm.password);
  formData.append("sex", String(registerForm.sex));
  formData.append("phoneNum", registerForm.phoneNum || "");
  formData.append("email", registerForm.email || "");
  formData.append("birth", formatBirthForBackend(registerForm.birth));
  formData.append("introduction", registerForm.introduction || "");
  formData.append("location", registerForm.location || "");

  const result = await fetchSignUpWithAvatar(formData);
  ElMessage({
    message: result.message,
    type: elMessageTypeFromResponse(result),
  });

  if (result.success) {
    changeActiveNav(NavLabel.SignIn);
    routerManager(RouterName.SignIn, { path: RouterName.SignIn });
  }
}

function goBack() {
  changeActiveNav(NavLabel.SignIn);
  routerManager(RouterName.SignIn, { path: RouterName.SignIn });
}
</script>

<style lang="scss" scoped>
.sign-btn:deep(.el-form-item__content) {
  display: flex;
  gap: 12px;
  width: 100%;
}

.sign-btn:deep(.el-button) {
  flex: 1;
}

.avatar-uploader {
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 100px;
  height: 100px;
  object-fit: cover;
}
</style>
