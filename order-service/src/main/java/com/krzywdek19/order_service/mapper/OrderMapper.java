package com.krzywdek19.order_service.mapper;

import com.krzywdek19.order_service.dto.OrderRequest;
import com.krzywdek19.order_service.dto.OrderResponse;
import com.krzywdek19.order_service.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);
    Order requestToOrder(OrderRequest orderRequest);
}
