package com.se.account.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "fund_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long securities_account_id; // 证券账户id
    private String transaction_pwd;     // 交易密码
    private String withdrawal_pwd;      // 取款密码
    private Integer status;             // 0 - 正常   1 - 挂失
    private Long creator;               // 创建账户的工作人员id
    private Date create_time;           // 创建时间
    private Date modify_time;           // 修改时间
    private boolean removed;            // 是否被移除
}
