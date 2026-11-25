package com.example.hua.common;

import lombok.Data;

/** 统一响应体；判断失败看 success/type——error() 的 code 仍是 200，只有 fatal() 才是 500 */
@Data
public class R {

    /** 业务状态码，默认 200，fatal 时 500。 */
    private int code;

    /** 给用户看的提示文案。 */
    private String message;

    /** 提示类型：success / warning / error / info，前端映射为 ElMessage 的 type。 */
    private String type;

    /** 是否成功，前端以此判断业务结果。 */
    private Boolean success;

    /** 业务数据，形态由具体接口决定。 */
    private Object data;

    /** 成功响应，不带数据 */
    public static R success(String message) {
        R r = new R();
        r.setCode(200);
        r.setMessage(message);
        r.setSuccess(true);
        r.setType("success");
        r.setData(null);
        return r;
    }

    /** 成功响应，带数据 */
    public static R success(String message, Object data) {
        R r = success(message);
        r.setData(data);
        return r;
    }

    /** 警告响应，业务未成功但不算错误，如「已收藏过」 */
    public static R warning(String message) {
        R r = error(message);
        r.setType("warning");
        return r;
    }

    /** 业务失败响应：code 仍为 200 */
    public static R error(String message) {
        R r = success(message);
        r.setSuccess(false);
        r.setType("error");
        return r;
    }

    /** 致命错误响应，未捕获异常时由全局异常处理器返回 */
    public static R fatal(String message) {
        R r = error(message);
        r.setCode(500);
        return r;
    }

}
