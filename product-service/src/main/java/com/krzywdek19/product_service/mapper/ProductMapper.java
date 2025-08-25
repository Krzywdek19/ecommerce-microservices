package com.krzywdek19.product_service.mapper;

import com.krzywdek19.product_service.dto.ProductCreateDTO;
import com.krzywdek19.product_service.dto.ProductResponseDTO;
import com.krzywdek19.product_service.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product dtoToProduct(ProductCreateDTO productCreateDTO);
    ProductResponseDTO productToDto(Product product);
}
