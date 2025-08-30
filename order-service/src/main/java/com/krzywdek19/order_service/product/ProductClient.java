package com.krzywdek19.order_service.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductClient {
    @PostMapping("/api/products/check-availability")
    List<ProductStockResponse> checkProductsAvailability(@RequestBody List<Long> productIds);

    @PostMapping("/api/products/decrease-stock")
    void decreaseStock(@RequestBody List<ProductStockRequest> productStockRequests);
}
