package com.krzywdek19.order_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private String orderNumber;
    private Instant orderDate;
    private String status;
    private List<OrderLineItemResponse> orderLineItems;

    @Getter
    @Setter
    public static class OrderLineItemResponse {
        private String skuCode;
        private int quantity;
        private double price;
    }
}
