package com.krzywdek19.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @Schema(description = "List of order line items", example = "[{\"productId\": \"123\", \"quantity\": 2}]")
    private List<OrderLineItemRequest> orderLineItems;
}