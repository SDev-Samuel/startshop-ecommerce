package com.startshop.ecommerce.service;

import com.startshop.ecommerce.dto.OrderItemResponse;
import com.startshop.ecommerce.dto.OrderResponse;
import com.startshop.ecommerce.entity.*;
import com.startshop.ecommerce.exception.BadRequestException;
import com.startshop.ecommerce.repository.CartItemRepository;
import com.startshop.ecommerce.repository.OrderRepository;
import com.startshop.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(CartItemRepository cartItemRepository,
                         OrderRepository orderRepository,
                         ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * "Checkout": convierte el carrito actual del usuario en un pedido.
     *
     * NOTA PEDAGOGICA: aqui es donde en el proyecto original se llamaba a Stripe
     * para cobrar de verdad. En esta version educativa SIMULAMOS el pago:
     * si hay stock suficiente, el pedido se marca directamente como PAGADO.
     * Cuando quieras dar el siguiente paso, este es el metodo donde conectarias
     * la API real de Stripe (crear un PaymentIntent antes de marcar PAGADO).
     */
    @Transactional
    public OrderResponse checkout(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Tu carrito esta vacio");
        }

        Order order = new Order(user, BigDecimal.ZERO, OrderStatus.PENDIENTE, LocalDateTime.now());
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("Stock insuficiente de " + product.getName());
            }

            // Descontamos el stock (simulando que la venta se concreto)
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(product, cartItem.getQuantity(), product.getPrice());
            order.addItem(orderItem);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setTotal(total);
        order.setStatus(OrderStatus.PAGADO); // Pago simulado exitoso
        orderRepository.save(order);

        // Vaciamos el carrito porque ya se convirtio en pedido
        cartItemRepository.deleteByUser(user);

        return toResponse(order);
    }

    public List<OrderResponse> getOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotal(),
                order.getStatus().name(),
                order.getCreatedAt(),
                items
        );
    }
}
