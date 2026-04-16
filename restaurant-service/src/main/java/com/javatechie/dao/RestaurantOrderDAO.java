package com.javatechie.dao;

import com.javatechie.dto.OrderResponseDTO;
import com.javatechie.entity.Order;
import com.javatechie.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestaurantOrderDAO {

    @Autowired
    private OrderRepository orderRepository;

    public OrderResponseDTO getOrders(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .map(this::convertToDto)
                .orElse(null);
    }

    public OrderResponseDTO saveOrder(OrderResponseDTO orderResponseDTO) {
        Order order = convertToEntity(orderResponseDTO);
        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    private OrderResponseDTO convertToDto(Order order) {
        return new OrderResponseDTO(
                order.getOrderId(),
                order.getName(),
                order.getQty(),
                order.getPrice(),
                order.getOrderDate(),
                order.getStatus(),
                order.getEstimateDeliveryWindow()
        );
    }

    private Order convertToEntity(OrderResponseDTO orderResponseDTO) {
        Order order = new Order();
        order.setOrderId(orderResponseDTO.getOrderId());
        order.setName(orderResponseDTO.getName());
        order.setQty(orderResponseDTO.getQty());
        order.setPrice(orderResponseDTO.getPrice());
        order.setOrderDate(orderResponseDTO.getOrderDate());
        order.setStatus(orderResponseDTO.getStatus());
        order.setEstimateDeliveryWindow(orderResponseDTO.getEstimateDeliveryWindow());
        return order;
    }
}
