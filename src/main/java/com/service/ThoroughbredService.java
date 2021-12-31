package com.service;

import java.util.List;

import com.entity.Thoroughbred;

public interface ThoroughbredService {
    Thoroughbred save(Thoroughbred thoroughbred);

    List<Thoroughbred> findAll();

    Thoroughbred findById(Long id);

    Thoroughbred update(Thoroughbred thoroughbred);

    void delete(Long id);
}
