import { api, type ApiResponse } from "@/utils/request";
import { jsonRequestConfig } from "@/api/types";

/** 注册表单数据；birth 在前端是 Date，提交前会被格式化为后端可解析的字符串。 */
export interface SignUpPayload {
  username: string;
  password: string;
  sex: number;
  phoneNum: string;
  email: string;
  birth: Date;
  introduction: string;
  location: string;
}

/** 登录表单数据，username 栏位也允许填邮箱。 */
export interface SignInPayload {
  username: string;
  password: string;
}

/** 把 Date 转成后端 datetime 能接受的 `yyyy-MM-dd 00:00:00` */
function formatBirthForBackend(value: Date): string {
  const year = value.getFullYear();
  const month = `${value.getMonth() + 1}`.padStart(2, "0");
  const day = `${value.getDate()}`.padStart(2, "0");
  return `${year}-${month}-${day} 00:00:00`;
}

const signUpFallback: ApiResponse<null> = {
  code: 500,
  success: false,
  message: "注册失败，请稍后重试",
  data: null,
};

const signInFallback: ApiResponse<null> = {
  code: 500,
  success: false,
  message: "登录失败，请稍后重试",
  data: null,
};

/** 注册（不带头像）。POST user/add */
export async function fetchSignUp(payload: SignUpPayload): Promise<ApiResponse> {
  try {
    return await api({
      method: "post",
      url: "user/add",
      data: {
        ...payload,
        birth: formatBirthForBackend(payload.birth),
      },
      config: jsonRequestConfig,
    });
  } catch (error: any) {
    const message =
      error?.data?.message || error?.response?.data?.message || signUpFallback.message;
    return {
      ...signUpFallback,
      message,
    };
  }
}

/** 注册（带头像）。POST user/register */
export async function fetchSignUpWithAvatar(formData: FormData): Promise<ApiResponse> {
  try {
    return await api({
      method: "post",
      url: "user/register",
      data: formData,
    });
  } catch (error: any) {
    const message =
      error?.data?.message || error?.response?.data?.message || signUpFallback.message;
    return {
      ...signUpFallback,
      message,
    };
  }
}

/** 登录。POST user/login/status */
export async function fetchSignIn(payload: SignInPayload): Promise<ApiResponse> {
  try {
    return await api({
      method: "post",
      url: "user/login/status",
      data: payload,
      config: jsonRequestConfig,
    });
  } catch (error: any) {
    const message =
      error?.data?.message || error?.response?.data?.message || signInFallback.message;
    return {
      ...signInFallback,
      message,
    };
  }
}

/** 发送验证码的返回数据；本地未配置 SMTP 时 debugCode 会直出验证码，方便联调。 */
export type SendPasswordResetCodeData = { debugCode?: string } | null;

/** 发送找回密码的验证码。GET user/sendVerificationCode?email=:email */
export async function fetchSendPasswordResetCode(
  email: string,
): Promise<ApiResponse<SendPasswordResetCodeData>> {
  try {
    return await api<SendPasswordResetCodeData>({
      method: "get",
      url: "user/sendVerificationCode",
      config: { params: { email } },
    });
  } catch (error: any) {
    const message =
      error?.data?.message || error?.response?.data?.message || "发送失败，请稍后重试";
    return { code: 500, success: false, message, data: null };
  }
}

/** 重置密码表单数据；confirmPassword 只在前端校验，不提交给后端。 */
export interface ResetPasswordPayload {
  email: string;
  code: string;
  password: string;
  confirmPassword: string;
}

const resetPasswordFallback: ApiResponse<null> = {
  code: 500,
  success: false,
  message: "重置失败，请稍后重试",
  data: null,
};

/** 校验验证码并重置密码。POST user/resetPassword */
export async function fetchResetPassword(payload: ResetPasswordPayload): Promise<ApiResponse> {
  try {
    return await api({
      method: "post",
      url: "user/resetPassword",
      data: payload,
      config: jsonRequestConfig,
    });
  } catch (error: any) {
    const message =
      error?.data?.message || error?.response?.data?.message || resetPasswordFallback.message;
    return {
      ...resetPasswordFallback,
      message,
    };
  }
}
