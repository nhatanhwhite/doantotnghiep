package com.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dto.ShoppingCartDTO;
import com.entity.Category;
import com.entity.Order;
import com.entity.OrderDetail;
import com.entity.Product;
import com.entity.UserSystem;
import com.repository.CartRepository;
import com.repository.DiscountCodeRepository;
import com.repository.OrderProductRepository;
import com.repository.OrderRepository;
import com.repository.ProductRepository;
import com.repository.UserSystemRepository;
import com.request.OrderRequest;
import com.service.OrderService;
import org.springframework.security.core.Authentication;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserSystemRepository userSystemRepository;

	@Override
	public Order save(OrderRequest request) {
		try {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.registerModule(new JavaTimeModule());
			// convert request data -> entity
			Order order = new Order();
			order.setCustomerName(request.getCustomerName());
			order.setCustomerEmail(request.getCustomerEmail());
			order.setCustomerPhone(request.getCustomerPhone());
			order.setAddress(request.getAddress());
			order.setStatus(1);
			order.setCreatedDate(new Date().getTime());
			List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
			for (ShoppingCartDTO cartItem : request.getOrderDetail()) {
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setOrder(order);
				orderDetail.setQuantity(cartItem.getQuantity());
				Product product = productRepository.findById(Long.valueOf(cartItem.getProductId())).get();
				int quantity = product.getInventory() - cartItem.getQuantity();
				product.setInventory(quantity);
				productRepository.save(product);
				orderDetail.setProduct(product);
				orderDetailList.add(orderDetail);
			}
			order.setOrderDetails(orderDetailList);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserSystem userSystem = userSystemRepository.findByEmail(authentication.getName());
			order.setUserSystem(userSystem);
//            List<Cart> carts = cartRepository.findByUserSystemOrderById(userSystem);
//
//            if(!discount.equals("0")) {
//                Optional<DiscountCode> discountCode = discountCodeRepository.findById(Long.parseLong(discount));
//                orderProduct.setDiscountCode(discountCode.get());
//            }
//
//            ShippingFee shippingFee = mapper.readValue(shipping, ShippingFee.class);

//            orderProduct.setLastUpdate(LocalDate.now());
//            orderProduct.setStatus(1);
//            orderProduct.setCarts(carts);
//            orderProduct.setShippingFee(shippingFee);
//            orderProduct.setUserSystem(userSystem);
//
			return orderRepository.save(order);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public Order findById(Long id) {
		Optional<Order> order = orderRepository.findById(id);

		if (order.isPresent()) {
			return order.get();
		}

		return null;
	}

	@Override
	public List<Order> findAllByUser(String username) {
		return orderRepository.findByuserSystem_email(username);
	}

}
