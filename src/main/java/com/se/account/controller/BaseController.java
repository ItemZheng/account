package com.se.account.controller;

import com.se.account.domain.Admin;
import com.se.account.domain.WebResponse;
import com.se.account.util.Constant;
import com.se.account.util.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

public class BaseController {
    @Autowired
    private HttpSession session;

    // provide basic method to build response
    Object buildSuccessResp(Object data){
        return new WebResponse(0, "Success", data);
    }

    Object buildResponse(int code, String msg, Object data){
        return new WebResponse(code, msg, data);
    }

    Object buildResponse(ErrorEnum errorEnum){
        if(errorEnum == null){
            errorEnum = ErrorEnum.ERROR_UNKNOWN;
        }
        return new WebResponse(errorEnum.getCode(), errorEnum.getEnDes() + "(" + errorEnum.getChDes() + ")", null);
    }

    Admin getAdmin(){
        return (Admin) session.getAttribute(Constant.SESSION_ACCOUNT_KEY);
    }

    void setAdmin(Admin admin){
        session.setAttribute(Constant.SESSION_ACCOUNT_KEY, admin);
    }
}
