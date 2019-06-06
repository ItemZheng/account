package com.se.account.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "account_balance")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 账户余额id

    @Column(name = "fund_account_id")
    private Long fundAccountId;         // 关联的资金账户id
    private int currency;               // 0 - 人命币   1 - 美元  .......
    private double balance;             // 余额
    private double available_balance;   // 可用账户余额（购买股票后，有部分资金会冻结）
    private long create_staff;          // 创建账户的工作人员id
    private Date create_time;           // 余额创建时间
    private long modify_staff;          // 修改账户的工作人员id
    private Date modify_time;           // 余额修改时间
    private int status;                 // 0 - 正常   1 - 冻结
    private boolean removed;            // 是否删除
}
