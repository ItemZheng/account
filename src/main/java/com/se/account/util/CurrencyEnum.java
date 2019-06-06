package com.se.account.util;

public enum CurrencyEnum {
    CURRENCY_RMB(0, "RMB"),
    CURRENCY_USD(1, "USD"),
    ;
    int type;
    String name;
    CurrencyEnum(int type, String name){
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
