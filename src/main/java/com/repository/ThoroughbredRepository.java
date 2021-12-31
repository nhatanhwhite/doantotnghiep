package com.repository;

import com.entity.Thoroughbred;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThoroughbredRepository extends JpaRepository<Thoroughbred, Long> {
    
}
