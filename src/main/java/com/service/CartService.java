package com.service;

import java.util.List;

import com.entity.Cart;
import com.service.dto.CartDTO;
import com.service.dto.CartTotalDTO;

public interface CartService {
    Cart save(String productId, String quantity);

    String cartQuantity();

    List<CartDTO> findAll();

    CartTotalDTO totalCart();

    Cart update(String id, String quantity);

    void delete(Long id);
}
