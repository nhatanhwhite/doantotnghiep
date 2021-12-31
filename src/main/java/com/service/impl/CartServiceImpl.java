package com.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.entity.Cart;
import com.entity.Product;
import com.entity.UserSystem;
import com.repository.CartRepository;
import com.repository.ProductRepository;
import com.repository.UserSystemRepository;
import com.service.CartService;
import com.service.dto.CartDTO;
import com.service.dto.CartTotalDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserSystemRepository userSystemRepository;

    @Override
    public Cart save(String productId, String quantity) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserSystem userSystem = userSystemRepository.findByEmail(authentication.getName());

            List<Cart> carts = cartRepository.findByUserSystemOrderById(userSystem);
            Map<Long, Cart> map = new HashMap<>();

            if(carts.size() > 0) {
                for(Cart cart : carts) {
                    map.put(cart.getProduct().getId(), cart);
                }
            }

            Optional<Product> productOptional = productRepository.findById(Long.parseLong(productId));

            if(productOptional.isPresent()) {
                Cart cart = new Cart();
                Cart cartData = map.get(productOptional.get().getId());

                if(cartData != null) {
                    cart.setId(cartData.getId());
                    cart.setQuantity(cartData.getQuantity() + Integer.parseInt(quantity));
                    cart.setProduct(cartData.getProduct());
                    cart.setLastUpdate(LocalDate.now());
                    cart.setUserSystem(userSystem);
                } else {
                    cart.setProduct(productOptional.get());
                    cart.setQuantity(Integer.parseInt(quantity));
                    cart.setLastUpdate(LocalDate.now());
                    cart.setUserSystem(userSystem);
                }

                Cart result = cartRepository.save(cart);

                return result;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String cartQuantity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSystem userSystem = userSystemRepository.findByEmail(authentication.getName());

        int quantity = 0;
        
        List<Cart> carts = cartRepository.findByUserSystemOrderById(userSystem);
        if(carts.size() > 0) {
            for(Cart cart: carts) {
                quantity = quantity + cart.getQuantity();
            }
        }

        String total = String.valueOf(quantity);

        return total;
    } 

    @Override
    public List<CartDTO> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSystem userSystem = userSystemRepository.findByEmail(authentication.getName());

        List<Cart> carts = cartRepository.findByUserSystemOrderById(userSystem);
        List<CartDTO> cartDTOs = new ArrayList<>();

        if(carts.size() > 0) {
            for(Cart cart : carts) {
                CartDTO cartDTO = new CartDTO();

                cartDTO.setCart(cart);
                cartDTO.setPrice(cart.getProduct().getPriceSell());
                cartDTO.setQuantity(cart.getQuantity());

                if(cart.getProduct().getSale() > 0) {
                    float percentage =  cart.getProduct().getPriceSell() * (float)cart.getProduct().getSale() / 100;
                    float priceSale = cart.getProduct().getPriceSell() - percentage;

                    cartDTO.setPriceSale((int)priceSale);
                    cartDTO.setSale(cart.getProduct().getSale());

                    cartDTO.setTotal((int)priceSale * cart.getQuantity());
                } else {
                    cartDTO.setPriceSale(0);
                    cartDTO.setSale(0);
                    cartDTO.setTotal(cart.getProduct().getPriceSell() * cart.getQuantity());
                }

                cartDTOs.add(cartDTO);
            }
        }

        return cartDTOs;
    }

    @Override
    public CartTotalDTO totalCart() {
        CartTotalDTO cartTotalDTO = new CartTotalDTO();

        List<CartDTO> cartDTOs = this.findAll();

        int total = 0;
        int totalQuantity = 0;

        for(CartDTO cartDTO : cartDTOs) {
            totalQuantity = totalQuantity + cartDTO.getQuantity();
            total = total + cartDTO.getTotal();
        }
        cartTotalDTO.setCartDTOs(cartDTOs);
        cartTotalDTO.setTotalQuantity(totalQuantity);
        cartTotalDTO.setTotal(total);

        return cartTotalDTO;
    }

    @Override
    public Cart update(String id, String quantity) {
        Optional<Cart> cartOptional = cartRepository.findById(Long.parseLong(id));

        int currentQuantity = Integer.parseInt(quantity);

        if(cartOptional.isPresent()) {
            Cart cart = new Cart();

            cart.setId(cartOptional.get().getId());
            cart.setProduct(cartOptional.get().getProduct());
            cart.setQuantity(currentQuantity);
            cart.setLastUpdate(LocalDate.now());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserSystem userSystem = userSystemRepository.findByEmail(authentication.getName());
            cart.setUserSystem(userSystem);

            return cartRepository.save(cart);
        }

        return null;
    }

    @Override
    public void delete(Long id) {
        cartRepository.deleteById(id);
    }
}
