package com.startshop.ecommerce.controller;

import com.startshop.ecommerce.dto.OrderResponse;
import com.startshop.ecommerce.entity.User;
import com.startshop.ecommerce.exception.ResourceNotFoundException;
import com.startshop.ecommerce.repository.UserRepository;
import com.startshop.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    private User currentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(Authentication authentication) {
        return ResponseEntity.ok(orderService.checkout(currentUser(authentication)));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrders(currentUser(authentication)));
    }
}
