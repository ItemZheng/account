package com.se.account.interceptor;

import com.se.account.annotation.LoginIgnore;
import com.se.account.util.Constant;
import com.se.account.util.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

// 登陆拦截器，所有的请求都会事先经过该函数
@Component
@Slf4j
public class LoginHandleInterceptor extends BaseInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取到处理请求的 controller method
        if (!(handler instanceof HandlerMethod)) {
            log.info("Handle method class not match");
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();

        // 判断该 controller method 是否有LoginIgnore注解
        LoginIgnore loginIgnore = method.getAnnotation(LoginIgnore.class);
        if (loginIgnore != null) {
            // 无需判断是否登陆
            return true;
        }
        // 需要登陆
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT_KEY) != null) {
            return true;
        }
        log.warn("User Not Login");
        writeResponse(ErrorEnum.ERROR_USER_NOT_LOGIN, response);
        return false;
    }
}
