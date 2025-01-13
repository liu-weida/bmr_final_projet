package org.rhw.bmr.user.common.enums;

import org.rhw.bmr.user.common.convention.errorcode.IErrorCode;

public enum UserErrorCodeEnum implements IErrorCode {

    USER_NULL("B000200", "用户信息不存在"),
    USER_NAME_EXIST("B000201", "用户已经存在"),
    USER_SAVE_ERROR("B000202", "用户新增失败"),
    USER_REGISTER_RATE_ERROR("B000203", "已经达到并行注册上限"),
    USER_LOGIN_REPEAT("B000204", "用户重复登录"),
    USER_NOT_ONLINE("B000205", "用户尚未登录"),
    USER_TOKEN_FAIL("A00200", "用户Token验证失效");

    private final String code;
    private final String message;

    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
