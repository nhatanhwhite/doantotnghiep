package com.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.entity.Thoroughbred;
import com.entity.UserSystem;
import com.repository.ThoroughbredRepository;
import com.repository.UserSystemRepository;
import com.service.ThoroughbredService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ThoroughbredServiceImpl implements ThoroughbredService {
    
    @Autowired
    private ThoroughbredRepository thoroughbredRepository;

    @Autowired
    private UserSystemRepository userSystemRepository;

    @Override
    public Thoroughbred save(Thoroughbred thoroughbred) {
        try{
            thoroughbred.setLastUpdate(LocalDate.now());
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserSystem userSystem = userSystemRepository.findByEmail(authentication.getName());

            thoroughbred.setUserSystem(userSystem);

            return thoroughbredRepository.save(thoroughbred);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Thoroughbred> findAll() {
        List<Thoroughbred> thoroughbreds = thoroughbredRepository.findAll();

        return thoroughbreds;
    }

    @Override
    public Thoroughbred findById(Long id) {
        Optional<Thoroughbred> thoroughbred = thoroughbredRepository.findById(id);
        
        if(thoroughbred.isPresent()) {
            return thoroughbred.get();
        }

        return null;
    }

    @Override
    public Thoroughbred update(Thoroughbred thoroughbred) {
        try{
            thoroughbred.setLastUpdate(LocalDate.now());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserSystem userSystem = userSystemRepository.findByEmail(authentication.getName());

            thoroughbred.setUserSystem(userSystem);

            return thoroughbredRepository.save(thoroughbred);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void delete(Long id) {
        thoroughbredRepository.deleteById(id);
    }


}
