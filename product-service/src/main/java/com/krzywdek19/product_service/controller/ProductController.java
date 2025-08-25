package com.krzywdek19.product_service.controller;

import com.krzywdek19.product_service.dto.ProductCreateDTO;
import com.krzywdek19.product_service.dto.ProductResponseDTO;
import com.krzywdek19.product_service.dto.ProductUpdateDTO;
import com.krzywdek19.product_service.model.Category;
import com.krzywdek19.product_service.model.Product;
import com.krzywdek19.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreateDTO product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateDTO product
    ) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable Category category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.getProductsByPriceRange(minPrice, maxPrice));
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Boolean> isProductInStock(@PathVariable Long id) {
        return ResponseEntity.ok(productService.isProductInStock(id));
    }

    @PostMapping("/{id}/reduce-stock")
    public ResponseEntity<?> reduceProductStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        boolean success = productService.reduceProductStock(id, quantity);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Not enough stock available"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(
            @RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    @GetMapping("/recently-added")
    public ResponseEntity<List<ProductResponseDTO>> getRecentlyAddedProducts(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(productService.getRecentlyAddedProducts(limit));
    }

    @PostMapping("/check-availability")
    public ResponseEntity<Boolean> checkProductAvailability(
            @RequestBody List<Long> productIds) {
        return ResponseEntity.ok(productService.checkProductAvailability(productIds));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Product>> getProductsByIds(
            @RequestBody List<Long> productIds) {
        return ResponseEntity.ok(productService.getProductsByIds(productIds));
    }
}