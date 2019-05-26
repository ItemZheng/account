package com.se.account.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AdminInfo {
    private String name;
    private Date create_time;
    private Date modify_time;
}
