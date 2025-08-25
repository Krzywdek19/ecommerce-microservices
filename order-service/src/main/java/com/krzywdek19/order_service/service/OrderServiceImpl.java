package com.krzywdek19.order_service.service;

import com.krzywdek19.order_service.Order;
import com.krzywdek19.order_service.OrderStatus;
import com.krzywdek19.order_service.dto.OrderRequest;
import com.krzywdek19.order_service.dto.OrderResponse;

import java.util.List;

public class OrderServiceImpl implements OrderService{
    @Override
    public Order createOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public OrderResponse getOrder(Long id) {
        return null;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return List.of();
    }

    @Override
    public void cancelOrder(Long id) {

    }

    @Override
    public Order updateOrderStatus(Long id, OrderStatus orderStatus) {
        return null;
    }
}
