package com.ac.ecom.service.impl;

import com.ac.ecom.dto.OrderItemRequest;
import com.ac.ecom.dto.OrderItemResponse;
import com.ac.ecom.dto.OrderRequest;
import com.ac.ecom.dto.OrderResponse;
import com.ac.ecom.model.Order;
import com.ac.ecom.model.OrderItem;
import com.ac.ecom.model.Product;
import com.ac.ecom.repository.OrderRepository;
import com.ac.ecom.repository.ProductRepository;
import com.ac.ecom.service.EmailService;
import com.ac.ecom.service.OrderService;
import com.ac.ecom.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final EmailService emailService;

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {

        Order order = new Order();
        String orderId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("Placed");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : orderRequest.items()) {

            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())))
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        emailService.sendOrderConfirmation(savedOrder);

        List<OrderItemResponse> itemResponses =  new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemResponse orderItemResponse = new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            );

            itemResponses.add(orderItemResponse);
        }

        OrderResponse orderResponse = new OrderResponse(savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                itemResponses
                );

        return orderResponse;
    }

    @Override
    public List<OrderResponse> getAllOrdersResponses() {

        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {

            List<OrderItemResponse> itemResponses = new ArrayList<>();

            for (OrderItem item : order.getOrderItems()) {
                OrderItemResponse orderItemResponse = new OrderItemResponse(
                  item.getProduct().getName(),
                  item.getQuantity(),
                  item.getTotalPrice()
                );

                itemResponses.add(orderItemResponse);
            }

            OrderResponse orderResponse = new OrderResponse(

                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    itemResponses
            );

            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }
}
