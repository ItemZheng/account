package com.se.account.domain;

import lombok.Data;

@Data
public class WebResponse {
    int code;
    String msg;
    Object data;

    public WebResponse(int code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
