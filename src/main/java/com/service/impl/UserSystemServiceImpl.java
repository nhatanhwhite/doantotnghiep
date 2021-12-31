package com.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.entity.UserSystem;
import com.repository.UserSystemRepository;
import com.service.UserSystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserSystemServiceImpl implements UserSystemService {

    @Autowired
    private UserSystemRepository userSystemRepository;

    @Override
    public List<UserSystem> findAll() {
        List<UserSystem> userSystems = userSystemRepository.findByActive(1);

        return userSystems;
    }

    @Override
    public UserSystem findById(Long id) {
        Optional<UserSystem> userSystemOptional = userSystemRepository.findById(id);

        if(userSystemOptional.isPresent()) {
            return userSystemOptional.get();
        }

        return null;
    }

    @Override
    public UserSystem update(UserSystem userSystem) {
        if(userSystem != null) {
            userSystem.setLastUpdate(LocalDate.now());

            return userSystemRepository.save(userSystem);
        }

        return null;
    }

    @Override
    public void delete(Long id) {
        UserSystem userSystem = userSystemRepository.findById(id).get();
        userSystem.setActive(0);

        userSystemRepository.save(userSystem);
    }
    
}
