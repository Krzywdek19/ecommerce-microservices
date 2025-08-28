package com.krzywdek19.order_service.mapper;

import com.krzywdek19.order_service.dto.OrderStatusHistoryResponse;
import com.krzywdek19.order_service.model.OrderStatusHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderStatusHistoryMapper {
    OrderStatusHistoryResponse toOrderStatusHistoryResponse(OrderStatusHistory orderStatusHistory);
}
