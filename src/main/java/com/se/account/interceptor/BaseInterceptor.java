package com.se.account.interceptor;

import com.google.gson.Gson;
import com.se.account.domain.WebResponse;
import com.se.account.util.ErrorEnum;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseInterceptor extends WebContentInterceptor {
    @Resource
    Gson gson;

    protected void writeResponse(ErrorEnum errorEnum, HttpServletResponse httpServletResponse) {
        // WebResponse
        WebResponse webResponse = new WebResponse(errorEnum.getCode(),
                errorEnum.getEnDes() + "(" + errorEnum.getChDes() + ")", null);

        String responseJson = gson.toJson(webResponse);
        try {
            httpServletResponse.setHeader("Content-type", "text/plain;charset=UTF-8");
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.getOutputStream().write(responseJson.getBytes());
        } catch (IOException ignore) {}
    }

}
