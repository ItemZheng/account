package com.se.account.util;

import java.util.HashMap;
import java.util.Map;

public class Check {
    static Map<Integer, CurrencyEnum> supportCurrency;
    static {
        supportCurrency = new HashMap<>();
        for(CurrencyEnum currencyEnum : CurrencyEnum.values()){
            supportCurrency.put(currencyEnum.getType(), currencyEnum);
        }
    }

    public static boolean checkValidPassword(String password){
        if(password == null){
            return false;
        }
        // 密码长度, 6 - 20
        return password.length() >= 6 && password.length() <= 20;
    }

    public static boolean checkValidUsername(String username){
        if(username == null){
            return false;
        }

        // 用户名长度, 6 - 20
        if(username.length() < 6 || username.length() > 20){
            return false;
        }

        // valid char 0-9 a-z A-Z
        for(int i = 0; i < username.length(); i++){
            char ch = username.charAt(i);
            if(!Character.isDigit(ch) && !Character.isLetter(ch)){
                return false;
            }
        }
        return true;
    }

    public static boolean checkSupportCurrency(int currency){
        return supportCurrency.containsKey(currency);
    }
}
