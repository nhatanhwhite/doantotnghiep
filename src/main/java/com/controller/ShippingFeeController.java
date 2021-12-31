package com.controller;

import java.util.List;

import com.entity.ShippingFee;
import com.payload.MessageResponse;
import com.service.ShippingFeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/shipping-fee")
public class ShippingFeeController {
    
    @Autowired
    private ShippingFeeService shippingFeeService;

    @PostMapping("/save")
    public ResponseEntity<MessageResponse> save (@RequestBody ShippingFee shippingFee) {
        ShippingFee result = shippingFeeService.save(shippingFee);
        
        if(result != null) {
            return ResponseEntity.ok(new MessageResponse("success"));
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<ShippingFee>> findAll() {
        List<ShippingFee> shippingFees = shippingFeeService.findAll();

        return ResponseEntity.ok().body(shippingFees);
    }

    @GetMapping("/find-id/{id}")
    public ResponseEntity<ShippingFee> findById(@PathVariable Long id) {
        ShippingFee shippingFee = shippingFeeService.findById(id);

        return ResponseEntity.ok().body(shippingFee);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> update(@RequestBody ShippingFee shippingFee) {
        ShippingFee result = shippingFeeService.update(shippingFee);

        if(result != null) {
            return ResponseEntity.ok(new MessageResponse("success"));
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        try{
            shippingFeeService.delete(id);
            return ResponseEntity.ok(new MessageResponse("success"));
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }
    
}
