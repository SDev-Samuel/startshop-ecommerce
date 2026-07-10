package com.startshop.ecommerce.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        String imageUrl,
        BigDecimal price,
        int quantity,
        BigDecimal subtotal
) {
}
