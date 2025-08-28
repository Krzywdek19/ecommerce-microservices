package com.krzywdek19.order_service.service;

import com.krzywdek19.order_service.model.OrderStatus;
import com.krzywdek19.order_service.dto.OrderRequest;
import com.krzywdek19.order_service.dto.OrderResponse;
import com.krzywdek19.order_service.exception.InvalidOrderStatusException;
import com.krzywdek19.order_service.exception.OrderNotFoundException;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    OrderResponse getOrder(Long id) throws OrderNotFoundException;
    Page<OrderResponse> getAllOrders(int page, int size);
    Page<OrderResponse> getOrdersByStatus(OrderStatus status, int page, int size);
    Page<OrderResponse> getOrdersByCustomerId(Long customerId, int page, int size);
    Page<OrderResponse> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size);
    OrderResponse updateOrderStatus(Long id, OrderStatus orderStatus) throws OrderNotFoundException, InvalidOrderStatusException;
    OrderResponse cancelOrder(Long id) throws OrderNotFoundException;
    List<OrderStatus> getOrderStatusHistory(Long id) throws OrderNotFoundException;
    boolean isOrderPaid(Long id) throws OrderNotFoundException;
}