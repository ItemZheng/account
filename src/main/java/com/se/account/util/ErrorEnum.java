package com.se.account.util;

public enum ErrorEnum {
    ERROR_UNKNOWN(9999, "Unknown Error", "未知错误"),
    ERROR_SERVER_INTERNAL_ERROR(10000, "Internal Server Error", "服务器内部错误"),
    ERROR_USERNAME_PASSWORD_ERROR(1, "Username Or Password Error", "用户名或密码错误"),
    ERROR_USER_NOT_LOGIN(2, "User Not Login In", "用户未登陆"),
    ERROR_PASSWORD_INVALID(4, "Password Is Invalid", "非法密码"),
    ERROR_USERNAME_EXIST(5, "Username Exists", "用户名已经存在"),
    ERROR_USERNAME_INVALID(6, "Username Is Invalid", "非法用户名"),
    ERROR_UNSUPPORT_CURRENCY(7, "Unsupport currency", "不支持该币种"),
    ERROR_ACCOUNT_NOT_EXIST(8, "Account not exist", "资金账户不存在"),
    ERROR_ACCOUNT_STATUS_ERROR(9, "Account status error", "资金账户状态异常"),
    ERROR_BALANCE_NOT_EXIST(10, "Balance not exist", "余额信息不存在"),
    ERROR_BALANCE_STATUS_ERROR(11, "Balance status error", "资金余额状态异常"),
    ERROR_WITHDRAWAL_PASSWORD_ERROR(12, "Withdrawal password error", "存款密码错误"),
    ERROR_AMOUNT_ERROR(13, "Amount error", "金额设置错误"),
    ERROR_BALANCE_NOT_ENOUGH(14, "Balance not enough", "余额不足"),
    ERROR_UNKNOWN_BALANCE_OPERATE_TYPE(15, "Unknow operate type", "未知余额操作类型"),
    ERROR_PASSWORD_NOT_MATCH(16, "Password not match", "密码不匹配"),
    ERROR_SECURITIES_ACCOUNT_HAS_FUND_ACCOUNT(17, "Securities account has fund account", "证券账户已关联资金账户"),
    ERROR_SECURITIES_ACCOUNT_NOT_HAS_FUND_ACCOUNT(18, "Securities account not has fund account", "该证券账户未关联资金账户"),
    ERROR_ACCOUNT_ALREADY_REPORT_LOSS(19, "Account already report loss", "资金账户已挂失"),
    ERROR_ACCOUNT_NOT_REPORT_LOSS(20, "Account not report loss", "账户未挂失"),
    ERROR_SECURITIES_ACCOUNT_NOT_MATCH_FUND_ACCOUNT(21, "Securities account not match fund account", "证券账户与资金账户不匹配"),
    ERROR_BALANCE_IS_NOT_ZERO(22, "Balance is not zero", "资金余额不为0"),
    ERROR_RECORD_NOT_FOUND(23, "Record not found", "记录未找到"),
    ERROR_ALREADY_DECREASED(24, "Already been decreased", "该记录已经扣款"),
    ERROR_ALREADY_RECOVERED(25, "Already been recovered", "该记录已经恢复"),
    ERROR_RECORD_TYPE_ERROR(26, "Record type error", "记录类型错误"),

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
