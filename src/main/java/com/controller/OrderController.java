package com.controller;

import java.util.List;

import com.entity.UserSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Category;
import com.entity.Order;
import com.payload.MessageResponse;
import com.repository.OrderRepository;
import com.request.OrderRequest;
import com.response.OrderResponse;
import com.service.OrderService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	OrderRepository orderRepository;

	@PostMapping
	public ResponseEntity<MessageResponse> save(@RequestBody OrderRequest request) {
		Order result = orderService.save(request);

		if (result != null) {
			return ResponseEntity.ok(new MessageResponse("successfully"));
		}

		return ResponseEntity.ok(new MessageResponse("failed"));
	}
	
	@GetMapping
    public ResponseEntity<List<Order>> findAll() {
        List<Order> orders = orderService.findAll();

        return ResponseEntity.ok(orders);
    }

	@GetMapping("/list")
	public ResponseEntity<List<Order>> findAllByCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		List<Order> orders = orderService.findAllByUser(username);

		return ResponseEntity.ok(orders);
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
		OrderResponse response = new OrderResponse();
		Order order = orderService.findById(id);
		response.setCustomerName(order.getCustomerName());
		response.setCustomerPhone(order.getCustomerPhone());
		response.setCustomerEmail(order.getCustomerEmail());
		response.setAddress(order.getAddress());
		response.setOrderDetails(order.getOrderDetails());
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/thanh-toan/{id}")
    public ResponseEntity<OrderResponse> thanhToan(@PathVariable Long id) {
		OrderResponse response = new OrderResponse();
		Order order = orderService.findById(id);
		order.setStatus(2);
		orderRepository.save(order);
        return ResponseEntity.ok(response);
    }
}
