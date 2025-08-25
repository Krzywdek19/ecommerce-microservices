package com.krzywdek19.product_service.dto;

import com.krzywdek19.product_service.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Category category;
    private String skuCode;
    private Integer quantity;
}