package com.krzywdek19.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String skuCode;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}