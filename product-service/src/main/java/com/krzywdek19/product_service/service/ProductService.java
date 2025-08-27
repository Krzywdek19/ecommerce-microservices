package com.krzywdek19.product_service.service;

import com.krzywdek19.product_service.model.Category;
import com.krzywdek19.product_service.dto.ProductCreateDTO;
import com.krzywdek19.product_service.dto.ProductResponseDTO;
import com.krzywdek19.product_service.dto.ProductUpdateDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductCreateDTO product);
    ProductResponseDTO updateProduct(Long id, ProductUpdateDTO product);
    ProductResponseDTO getProduct(Long id);
    Page<ProductResponseDTO> getProducts(int page, int size);
    void deleteProduct(Long id);
    Page<ProductResponseDTO> getProductsByCategory(Category category, int page, int size);
    Page<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int page, int size);
    boolean isProductInStock(Long id);
    boolean reduceProductStock(Long id, Integer quantity);
    Page<ProductResponseDTO> searchProducts(String keyword, int page, int size);
    List<ProductResponseDTO> getRecentlyAddedProducts(int limit);
    boolean checkProductAvailability(List<Long> productIds);
    List<ProductResponseDTO> getProductsByIds(List<Long> productIds);
}