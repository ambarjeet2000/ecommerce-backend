package com.ac.ecom.service;

import com.ac.ecom.dto.OrderRequest;
import com.ac.ecom.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrdersResponses();
}
