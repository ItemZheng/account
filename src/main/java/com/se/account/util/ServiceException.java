package com.se.account.util;

public class ServiceException extends Exception {
    private ErrorEnum errorEnum;

    public ServiceException(ErrorEnum errorEnum){
        this.errorEnum = errorEnum;
    }

    public ErrorEnum getErrorEnum() {
        return errorEnum;
    }
}
