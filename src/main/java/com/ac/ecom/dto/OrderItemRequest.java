package com.ac.ecom.dto;

public record OrderItemRequest(
        Integer productId,
        Integer quantity
) {
}
