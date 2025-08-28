package com.krzywdek19.order_service.service;

import com.krzywdek19.order_service.dto.OrderLineItemRequest;
import com.krzywdek19.order_service.exception.ProductNotFoundException;
import com.krzywdek19.order_service.mapper.OrderLineItemMapper;
import com.krzywdek19.order_service.mapper.OrderMapper;
import com.krzywdek19.order_service.mapper.OrderStatusHistoryMapper;
import com.krzywdek19.order_service.model.OrderStatus;
import com.krzywdek19.order_service.dto.OrderRequest;
import com.krzywdek19.order_service.dto.OrderResponse;
import com.krzywdek19.order_service.exception.InvalidOrderStatusException;
import com.krzywdek19.order_service.exception.OrderNotFoundException;
import com.krzywdek19.order_service.product.ProductClient;
import com.krzywdek19.order_service.repository.OrderLineItemRepository;
import com.krzywdek19.order_service.repository.OrderRepository;
import com.krzywdek19.order_service.repository.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderStatusHistoryMapper orderStatusHistoryMapper;
    private final OrderLineItemMapper orderLineItemMapper;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        List<Long> productIds = orderRequest.getOrderLineItems().stream()
                .map(OrderLineItemRequest::getProductId)
                .toList();

        var productsInStock = productClient.checkProductsAvailability(productIds);

        for(var lineItem : orderRequest.getOrderLineItems()) {
            var productStock = productsInStock.stream()
                    .filter(p -> p.getProductId().equals(lineItem.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundException("Product with ID " + lineItem.getProductId() + " not found in stock"));

            if(!productStock.isInStock() || productStock.getQuantity() < lineItem.getQuantity()) {
                throw new ProductNotFoundException("Product with ID " + lineItem.getProductId() + " is out of stock or insufficient quantity");
            }
        }

        var order = orderMapper.requestToOrder(orderRequest);

        order.setOrderNumber(generateOrderNumber());
        order.setOrderStatus(OrderStatus.CREATED);
        order.setOrderDate(LocalDateTime.now());

        var savedOrder = orderRepository.save(order);
        savedOrder.addStatusHistory(savedOrder.getOrderStatus(),"Order created");

        return orderMapper.toOrderResponse(orderRepository.save(savedOrder));
    }

    private String generateOrderNumber() {
        return "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                "-" + String.format("%04d", (int) (Math.random() * 10000));
    }

    @Override
    public OrderResponse getOrder(Long id) throws OrderNotFoundException {
        return null;
    }

    @Override
    public Page<OrderResponse> getAllOrders(int page, int size) {
        return null;
    }

    @Override
    public Page<OrderResponse> getOrdersByStatus(OrderStatus status, int page, int size) {
        return null;
    }

    @Override
    public Page<OrderResponse> getOrdersByCustomerId(Long customerId, int page, int size) {
        return null;
    }

    @Override
    public Page<OrderResponse> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        return null;
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, OrderStatus orderStatus) throws OrderNotFoundException, InvalidOrderStatusException {
        return null;
    }

    @Override
    public OrderResponse cancelOrder(Long id) throws OrderNotFoundException {
        return null;
    }

    @Override
    public List<OrderStatus> getOrderStatusHistory(Long id) throws OrderNotFoundException {
        return List.of();
    }

    @Override
    public boolean isOrderPaid(Long id) throws OrderNotFoundException {
        return false;
    }
}
