package com.se.account.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "balance_record")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 账户余额id
    private Long fund_account_id;       // 关联的资金账户id
    private int currency;               // 0 - 人命币   1 - 美元  .......
    private double balance;             // 余额
    private double available_balance;   // 可用账户余额（购买股票后，有部分资金会冻结）
    private Date create_time;           // 余额创建时间
    private Date modify_time;           // 余额修改时间
    private int status;                 // 0 - 正常   1 - 冻结
    private boolean removed;            // 是否删除
}
