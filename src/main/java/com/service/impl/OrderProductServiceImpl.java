package com.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.entity.Cart;
import com.entity.DiscountCode;
import com.entity.OrderProduct;
import com.entity.ShippingFee;
import com.entity.UserSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.repository.CartRepository;
import com.repository.DiscountCodeRepository;
import com.repository.OrderProductRepository;
import com.repository.UserSystemRepository;
import com.service.OrderProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    @Autowired
    private OrderProductRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    @Autowired
    private UserSystemRepository userSystemRepository;

    @Override
    public OrderProduct save(String discount, String shipping) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            OrderProduct orderProduct = new OrderProduct();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserSystem userSystem = userSystemRepository.findByEmail(authentication.getName());

            List<Cart> carts = cartRepository.findByUserSystemOrderById(userSystem);

            if(!discount.equals("0")) {
                Optional<DiscountCode> discountCode = discountCodeRepository.findById(Long.parseLong(discount));
                orderProduct.setDiscountCode(discountCode.get());
            }

            ShippingFee shippingFee = mapper.readValue(shipping, ShippingFee.class);

            orderProduct.setLastUpdate(LocalDate.now());
            orderProduct.setStatus(1);
            orderProduct.setCarts(carts);
            orderProduct.setShippingFee(shippingFee);
            orderProduct.setUserSystem(userSystem);

            return orderRepository.save(orderProduct);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
}
