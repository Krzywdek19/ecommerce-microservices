package com.krzywdek19.order_service.dto;

import com.krzywdek19.order_service.OrderLineItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private List<OrderLineItemDto> orderLineItems;

    @Getter
    @Setter
    public static class OrderLineItemDto {
        private String skuCode;
        private int quantity;
    }
}
