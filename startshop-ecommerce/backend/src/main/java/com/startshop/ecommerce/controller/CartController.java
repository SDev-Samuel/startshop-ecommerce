package com.startshop.ecommerce.controller;

import com.startshop.ecommerce.dto.CartItemRequest;
import com.startshop.ecommerce.dto.CartItemResponse;
import com.startshop.ecommerce.entity.User;
import com.startshop.ecommerce.exception.ResourceNotFoundException;
import com.startshop.ecommerce.repository.UserRepository;
import com.startshop.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    // El "email" del usuario logueado ya lo dejo Spring Security en el objeto Authentication
    // gracias a nuestro JwtAuthFilter. Aqui lo usamos para saber DE QUIEN es el carrito.
    private User currentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(Authentication authentication) {
        return ResponseEntity.ok(cartService.getCart(currentUser(authentication)));
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addItem(Authentication authentication,
                                                      @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(currentUser(authentication), request));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<CartItemResponse> updateItem(Authentication authentication,
                                                          @PathVariable Long id,
                                                          @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(currentUser(authentication), id, quantity));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(Authentication authentication, @PathVariable Long id) {
        cartService.removeItem(currentUser(authentication), id);
        return ResponseEntity.noContent().build();
    }
}
