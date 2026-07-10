package com.startshop.ecommerce.entity;

/**
 * Estados posibles de un pedido.
 * PENDIENTE -> se creo pero no se ha pagado.
 * PAGADO    -> el pago (simulado) fue exitoso.
 * CANCELADO -> el pedido fue cancelado.
 */
public enum OrderStatus {
    PENDIENTE,
    PAGADO,
    CANCELADO
}
