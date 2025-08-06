package com.krzywdek19.product_service;

import com.krzywdek19.product_service.dto.ProductCreateDTO;
import com.krzywdek19.product_service.dto.ProductResponseDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    Product dtoToProduct(ProductCreateDTO productCreateDTO);
    ProductResponseDTO productToDto(Product product);
}
