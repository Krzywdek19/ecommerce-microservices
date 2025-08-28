package com.krzywdek19.order_service.repository;

import com.krzywdek19.order_service.model.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
