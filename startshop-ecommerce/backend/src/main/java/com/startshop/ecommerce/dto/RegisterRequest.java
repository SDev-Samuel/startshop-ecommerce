package com.startshop.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Un "record" es una forma moderna y corta de Java para crear clases
 * que solo transportan datos (sin logica). Java genera automaticamente
 * el constructor, los getters (nombre(), email(), password()) y equals/hashCode.
 */
public record RegisterRequest(
        @NotBlank(message = "El nombre es obligatorio") String fullName,
        @NotBlank @Email(message = "El email no es valido") String email,
        @NotBlank @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres") String password
) {
}
