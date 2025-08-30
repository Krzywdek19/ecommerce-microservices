package com.krzywdek19.product_service.service;

import com.krzywdek19.product_service.dto.*;
import com.krzywdek19.product_service.model.Category;
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
    List<ProductResponseDTO> getProductsByIds(List<Long> productIds);
    List<ProductStockResponse> checkProductsAvailability(List<Long> productIds);
    void decreaseStock(List<ProductStockRequest> productStockRequests);
}