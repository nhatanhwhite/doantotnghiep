package com.service;

import java.util.List;

import com.entity.UserSystem;

public interface UserSystemService {
    List<UserSystem> findAll();

    UserSystem findById(Long id);

    UserSystem update(UserSystem user);

    void delete(Long id);
}
