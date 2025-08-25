package com.krzywdek19.order_service.service;

import com.krzywdek19.order_service.Order;
import com.krzywdek19.order_service.OrderStatus;
import com.krzywdek19.order_service.dto.OrderRequest;
import com.krzywdek19.order_service.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);
    OrderResponse getOrder(Long id);
    List<OrderResponse> getAllOrders();
    void cancelOrder(Long id);
    Order updateOrderStatus(Long id, OrderStatus orderStatus);
}