package com.suki.dto;

import lombok.Data;

@Data
public class JsonResult<T> {

    private Integer code;//错误代码
    private String msg;  //提示信息
    private T data;//返回数据


    public JsonResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonResult() {
    }


    public JsonResult fail() {
        return new JsonResult(1, "操作失败", null);
    }

    public JsonResult fail(String msg) {
        return new JsonResult(1, msg, null);
    }

    public  JsonResult ok() {
        return new  JsonResult(0, "操作成功", null);
    }


    public  JsonResult ok(T data) {
        return new  JsonResult(0, "操作成功", data);
    }
}
