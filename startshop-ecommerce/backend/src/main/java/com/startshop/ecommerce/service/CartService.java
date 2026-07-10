package com.startshop.ecommerce.service;

import com.startshop.ecommerce.dto.CartItemRequest;
import com.startshop.ecommerce.dto.CartItemResponse;
import com.startshop.ecommerce.entity.CartItem;
import com.startshop.ecommerce.entity.Product;
import com.startshop.ecommerce.entity.User;
import com.startshop.ecommerce.exception.BadRequestException;
import com.startshop.ecommerce.exception.ResourceNotFoundException;
import com.startshop.ecommerce.repository.CartItemRepository;
import com.startshop.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItemResponse> getCart(User user) {
        return cartItemRepository.findByUser(user).stream()
                .map(this::toResponse)
                .toList();
    }

    public CartItemResponse addItem(User user, CartItemRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (product.getStock() < request.quantity()) {
            throw new BadRequestException("No hay suficiente stock de " + product.getName());
        }

        // Si el producto ya esta en el carrito, solo sumamos la cantidad.
        CartItem item = cartItemRepository.findByUserAndProduct(user, product)
                .orElse(new CartItem(user, product, 0));

        item.setQuantity(item.getQuantity() + request.quantity());
        cartItemRepository.save(item);

        return toResponse(item);
    }

    public CartItemResponse updateQuantity(User user, Long itemId, int quantity) {
        if (quantity < 1) {
            throw new BadRequestException("La cantidad debe ser al menos 1");
        }

        CartItem item = cartItemRepository.findByIdAndUser(itemId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);
        return toResponse(item);
    }

    public void removeItem(User user, Long itemId) {
        CartItem item = cartItemRepository.findByIdAndUser(itemId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado"));
        cartItemRepository.delete(item);
    }

    private CartItemResponse toResponse(CartItem item) {
        BigDecimal subtotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getImageUrl(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                subtotal
        );
    }
}
