package com.krzywdek19.order_service.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductClient {
    @GetMapping("/api/products/{id}")
    ProductResponse getProduct(@PathVariable("id") Long id);

    @GetMapping("/api/products/check-quantity/{id}/{quantity}")
    boolean checkProductQuantity(@PathVariable("id") Long id, @PathVariable("quantity") Integer quantity);
}
