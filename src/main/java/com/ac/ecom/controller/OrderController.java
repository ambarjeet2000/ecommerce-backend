package com.ac.ecom.controller;

import com.ac.ecom.dto.OrderRequest;
import com.ac.ecom.dto.OrderResponse;
import com.ac.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {

        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> responses = orderService.getAllOrdersResponses();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}
