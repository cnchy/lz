package com.iotlab.integrityarchives.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author tycoding
 * @date 2019-03-09
 */
public enum StatusEnums {

    SUCCESS(200, "操作成功"),
    ACCOUNT_UNKNOWN(500, "工号或者姓名不存在"),
    ACCOUNT_REJECTED(500, "此账号目前无法登录"),
    ACCOUNT_ERROR(500,"用户名或密码错误"),
    SYSTEM_ERROR(500, "系统错误"),
    PARAM_ERROR(400, "参数错误"),
    PARAM_REPEAT(400, "工号已存在"),
    OTHER(-100, "其他错误");

    @Getter
    @Setter
    private int code;
    @Getter
    @Setter
    private String info;

    StatusEnums(int code, String info) {
        this.code = code;
        this.info = info;
    }
}
