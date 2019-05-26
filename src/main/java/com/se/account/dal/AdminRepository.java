package com.se.account.dal;

import com.se.account.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByNameAndEnable(String name, boolean enable);
}
