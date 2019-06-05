package com.se.account.service;

import com.se.account.dal.AdminRepository;
import com.se.account.domain.Admin;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {
    @Resource
    AdminRepository adminDb;

    public boolean exist(String name){
        return adminDb.findByNameAndRemoved(name, false) != null;
    }

    public Admin getAdminByName(String name){
        return adminDb.findByNameAndRemoved(name, false);
    }
}
