package com.controller;

import com.entity.OrderProduct;
import com.payload.MessageResponse;
import com.service.OrderProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/order-product")
public class OrderProductController {
    
    @Autowired
    private OrderProductService orderProductService;

    @PostMapping("/save")
    public ResponseEntity<MessageResponse> save(@RequestParam("discount") String discount, @RequestParam("shiiping") String shipping) {
        OrderProduct result = orderProductService.save(discount, shipping);

        if (result != null) {
            return ResponseEntity.ok(new MessageResponse("success"));
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }
}
