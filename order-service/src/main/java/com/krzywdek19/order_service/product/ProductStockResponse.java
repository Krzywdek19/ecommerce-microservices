package com.krzywdek19.order_service.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStockResponse {
    private Long productId;
    private Integer quantity;
    private boolean inStock;
}