package com.se.account.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AccountInfoDTO {
    private Long id;
    private Long securities_account_id; // 证券账户id
    private Integer status;             // 0 - 正常   1 - 挂失
    private long create_staff;          // 创建账户的工作人员id
    private Date create_time;           // 创建时间
    private long modify_staff;          // 修改账户的工作人员id
    private Date modify_time;           // 修改时间
    private BalanceInfoDTO balanceInfo;

    @Data
    public static class BalanceInfoDTO {
        private Long id;                    // 账户余额id
        private int currency;               // 0 - 人命币   1 - 美元  .......
        private double balance;             // 余额
        private double available_balance;   // 可用账户余额（购买股票后，有部分资金会冻结）
        private long create_staff;          // 创建账户的工作人员id
        private Date create_time;           // 余额创建时间
        private long modify_staff;          // 修改账户的工作人员id
        private Date modify_time;           // 余额修改时间
        private int status;                 // 0 - 正常   1 - 冻结
    }
}
