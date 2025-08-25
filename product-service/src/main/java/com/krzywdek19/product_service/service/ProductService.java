package com.krzywdek19.product_service.service;

import com.krzywdek19.product_service.model.Category;
import com.krzywdek19.product_service.model.Product;
import com.krzywdek19.product_service.dto.ProductCreateDTO;
import com.krzywdek19.product_service.dto.ProductResponseDTO;
import com.krzywdek19.product_service.dto.ProductUpdateDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    //basic CRUD operations
    Product createProduct(ProductCreateDTO product);
    Product updateProduct(Long id, ProductUpdateDTO product);
    ProductResponseDTO getProduct(Long id);
    List<ProductResponseDTO> getProducts();
    void deleteProduct(Long id);

    //logic operations
    List<ProductResponseDTO> getProductsByCategory(Category category);
    List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    boolean isProductInStock(Long id);
    boolean reduceProductStock(Long id, Integer quantity);
    List<ProductResponseDTO> searchProducts(String keyword);
    List<ProductResponseDTO> getRecentlyAddedProducts(int limit);
    boolean checkProductAvailability(List<Long> productIds);
    List<Product> getProductsByIds(List<Long> productIds);
}
