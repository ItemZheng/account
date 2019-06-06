package com.se.account.controller;

import com.se.account.annotation.LoginIgnore;
import com.se.account.dal.AdminRepository;
import com.se.account.domain.Admin;
import com.se.account.dto.AdminInfo;
import com.se.account.service.AdminService;
import com.se.account.util.Check;
import com.se.account.util.ErrorEnum;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController{
    @Resource
    AdminRepository adminDb;

    @Resource
    AdminService adminService;

    @RequestMapping("/login")
    @LoginIgnore
    public Object login(@NonNull String username,@NonNull String password){
        if(!Check.checkValidUsername(username)){
            return buildResponse(ErrorEnum.ERROR_USERNAME_INVALID);
        }

        // check if exist
        Admin admin = adminService.getAdminByName(username);
        if(admin == null){
            return buildResponse(ErrorEnum.ERROR_USERNAME_INVALID);
        }
        try {
            if(password.equals(admin.getPassword())){
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
    public Object add(@NonNull String username, @NonNull String password){
        // check valid password
        if(!Check.checkValidPassword(password)){
            return buildResponse(ErrorEnum.ERROR_PASSWORD_INVALID);
        }
        if(!Check.checkValidUsername(username)){
            return buildResponse(ErrorEnum.ERROR_USERNAME_INVALID);
        }

        // check if exist
        if(adminService.exist(username)){
            return buildResponse(ErrorEnum.ERROR_USERNAME_EXIST);
        }

        // new admin
        Admin admin = new Admin();
        Date date = new Date();
        admin.setName(username);
        admin.setPassword(password);
        admin.setModify_time(date);
        admin.setCreate_time(date);
        admin.setRemoved(false);
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
