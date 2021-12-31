package com.repository;

import com.entity.UserSystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSystemRepository extends JpaRepository<UserSystem, Long>{
    
    Boolean existsByEmailAndActive(String email, int active);

    Boolean existsByPhoneAndActive(String phone, int active);

    UserSystem findByEmail(String email);

    List<UserSystem> findByActive(int active);
}
