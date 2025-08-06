package com.krzywdek19.product_service;

import com.krzywdek19.product_service.dto.ProductCreateDTO;
import com.krzywdek19.product_service.dto.ProductResponseDTO;
import com.krzywdek19.product_service.dto.ProductUpdateDTO;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductCreateDTO product);
    Product updateProduct(Long id, ProductUpdateDTO product);
    ProductResponseDTO getProduct(Long id);
    List<ProductResponseDTO> getProducts();
    void deleteProduct(Long id);
}
