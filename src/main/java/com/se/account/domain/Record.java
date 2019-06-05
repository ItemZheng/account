package com.se.account.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "balance_record")
@Entity
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long balance_id;        // 关联的资金账户余额id
    private double amount;          // 余额的变化量
    private int operate_code;       // 101 - 存钱     102 - 利息发放  103 - 购买失败恢复冻结的资金
                                    // 201 - 冻结部分资金   202 - 取款    203 - 购买成功后扣款
    private int pre_decrease_id;    // 购买失败/成功 时对应的预扣款的资金，其他操作为0
    private Date create_time;       // 记录产生时间
    private Date modify_time;       // 记录修改时间（只有删除操作）
    private boolean removed;        // 是否被删除
}
