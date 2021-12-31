package com.service;

import com.entity.OrderProduct;

public interface OrderProductService {
    
    OrderProduct save(String discount, String shipping);
}
