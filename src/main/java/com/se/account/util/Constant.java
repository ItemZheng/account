package com.se.account.util;

public class Constant {
    // Session Key
    public static final String SESSION_ACCOUNT_KEY = "Admin";

    // 账户状态
    public static final int ACCOUNT_STATUS_NORMAL = 0;      // 正常
    public static final int ACCOUNT_STATUS_REPORT_LOSS = 1; // 挂失

    // 币种
    public static final int CURRENCY_TYPE_RMB = 0;  // 人民币
    public static final int CURRENCY_TYPE_USD = 1;  // 美元

    // 余额操作代码
    public static final int BALANCE_OPCODE_SAVE = 101;          // 存钱
    public static final int BALANCE_OPCODE_INTEREST = 102;      // 利息发放
    public static final int BALANCE_OPCODE_RECOVER = 103;       // 购买失败恢复
    public static final int BALANCE_OPCODE_BUY = 201;           // 冻结部分资金
    public static final int BALANCE_OPCODE_WITHDRAWAL = 202;    // 取款
    public static final int BALANCE_OPCODE_REDUCE = 203;        // 购买成功后扣款

    // 余额状态
    public static final int BALANCE_STATUS_NORMAL = 0;      // 正常
    public static final int BALANCE_STATUS_FREEZE = 1;      // 冻结

    // 证券账户Action
    public static final int ACTION_FREEZE_ACCOUNT = 0;
    public static final int ACTION_RECOVER_ACCOUNT = 1;
}
