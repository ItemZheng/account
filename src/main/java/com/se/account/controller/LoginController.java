package com.se.account.controller;

import com.se.account.annotation.LoginIgnore;
import com.se.account.dal.AdminRepository;
import com.se.account.domain.Admin;
import com.se.account.dto.AdminInfo;
import com.se.account.util.Check;
import com.se.account.util.ErrorEnum;
import com.se.account.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class LoginController extends BaseController{
    @Autowired
    AdminRepository adminDb;

    @RequestMapping("/login")
    @LoginIgnore
    public Object login(@NonNull String username,@NonNull String key, @NonNull String timestamp){
        if(!Check.checkValidUsername(username)){
            return buildResponse(ErrorEnum.ERROR_USERNAME_INVALID);
        }

        // check if exist
        List adminList = adminDb.findByNameAndEnable(username, true);
        if(adminList != null && adminList.size() != 1){
            return buildResponse(ErrorEnum.ERROR_USERNAME_INVALID);
        }
        Admin admin = (Admin) adminList.get(0);
        try {
            if(key.equals(Util.Md5(username + admin.getPassword() + timestamp))){
                setAdmin(admin);
                return buildSuccessResp(null);
            } else {
                return buildSuccessResp(ErrorEnum.ERROR_USERNAME_PASSWORD_ERROR);
            }
        } catch (Exception e){
            e.printStackTrace();
            return buildResponse(ErrorEnum.ERROR_SERVER_INTERNAL_ERROR);
        }
    }


    @RequestMapping("/add")
    public Object add(@NonNull String username, @NonNull String password, @NonNull String confirmPassword){
        // check valid password
        if(!Check.checkValidPassword(password)){
            return buildResponse(ErrorEnum.ERROR_Password_INVALID);
        }
        if(!Check.checkValidUsername(username)){
            return buildResponse(ErrorEnum.ERROR_USERNAME_INVALID);
        }

        // check
        if(!password.equals(confirmPassword)){
            return buildResponse(ErrorEnum.ERROR_Password_NOT_SAME);
        }

        // check if exist
        List adminList = adminDb.findByNameAndEnable(username, true);
        if(adminList != null && adminList.size() > 0){
            return buildResponse(ErrorEnum.ERROR_USERNAME_EXIST);
        }

        // new admin
        Admin admin = new Admin();
        Date date = new Date();
        admin.setName(username);
        admin.setPassword(password);
        admin.setModify_time(date);
        admin.setCreate_time(date);
        admin.setEnable(false);
        adminDb.save(admin);
        return buildSuccessResp(null);
    }

    @RequestMapping("/info")
    public Object info(){
        // get admin
        Admin admin = getAdmin();

        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setCreate_time(admin.getCreate_time());
        adminInfo.setModify_time(admin.getModify_time());
        adminInfo.setName(admin.getName());
        return buildSuccessResp(adminInfo);
    }
}
