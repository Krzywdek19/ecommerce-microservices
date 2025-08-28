package com.krzywdek19.order_service.dto;

import com.krzywdek19.order_service.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusHistoryResponse {
    private Long id;
    private OrderStatus status;
    private LocalDateTime timestamp;
    private String comment;
}