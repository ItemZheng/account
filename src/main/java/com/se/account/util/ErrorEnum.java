package com.se.account.util;

public enum ErrorEnum {
    ERROR_UNKNOWN(9999, "Unknown Error", "未知错误"),
    ERROR_SERVER_INTERNAL_ERROR(10000, "Internal Server Error", "服务器内部错误"),
    ERROR_USERNAME_PASSWORD_ERROR(1, "Username Or Password Error", "用户名或密码错误"),
    ERROR_USER_NOT_LOGIN(2, "User Not Login In", "用户未登陆"),
    ERROR_Password_NOT_SAME(3, "Password Is Not Same", "两次密码输入不一致"),
    ERROR_Password_INVALID(4, "Password Is Invalid", "非法密码"),
    ERROR_USERNAME_EXIST(5, "Username Exists", "用户名已经存在"),
    ERROR_USERNAME_INVALID(6, "Username Is Invalid", "非法用户名"),
    ;
    int code;       // error code
    String enDes;   // english error description
    String chDes;   // chinese error description
    ErrorEnum(int code, String enDes, String chDes){
        this.chDes = chDes;
        this.code = code;
        this.enDes = enDes;
    }

    public int getCode() {
        return code;
    }

    public String getEnDes() {
        return enDes;
    }

    public String getChDes() {
        return chDes;
    }
}
