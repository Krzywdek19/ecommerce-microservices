package com.krzywdek19.order_service.repository;

import com.krzywdek19.order_service.model.OrderStatus;
import com.krzywdek19.order_service.model.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    List<OrderStatusHistory> findByOrderIdOrderByTimestampDesc(Long orderId);
}
