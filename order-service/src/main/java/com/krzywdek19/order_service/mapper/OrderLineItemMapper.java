package com.krzywdek19.order_service.mapper;

import com.krzywdek19.order_service.dto.OrderLineItemResponse;
import com.krzywdek19.order_service.model.OrderLineItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderLineItemMapper {
    OrderLineItemResponse toOrderLineItemResponse(OrderLineItem orderLineItem);
    OrderLineItem toOrderLineItem(OrderLineItem orderLineItem);
}
