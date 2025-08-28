package com.krzywdek19.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockResponse {
    private Long productId;
    private Integer quantity;
    private boolean inStock;
}