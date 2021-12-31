package com.service;

import java.util.List;

import com.entity.Category;
import com.entity.Order;
import com.entity.OrderProduct;
import com.request.OrderRequest;

public interface OrderService {
    
    Order save(OrderRequest request);
    List<Order> findAll();
    
    Order findById(Long id);

    List<Order> findAllByUser(String username);
}
