package com.startshop.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequest(
        @NotNull(message = "El productId es obligatorio") Long productId,
        @Min(value = 1, message = "La cantidad minima es 1") int quantity
) {
}
