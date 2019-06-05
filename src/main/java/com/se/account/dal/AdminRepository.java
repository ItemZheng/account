package com.se.account.dal;

import com.se.account.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByNameAndRemoved(String name, boolean removed);
}
