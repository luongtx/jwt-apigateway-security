package com.javatechie.service;

import com.javatechie.dao.RestaurantOrderDAO;
import com.javatechie.dto.OrderRequestDTO;
import com.javatechie.dto.OrderResponseDTO;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantOrderDAO orderDAO;

    @Autowired
    private OutboxEventService outboxEventService;

    public String greeting() {
        return "Welcome to Swiggy Restaurant service";
    }

    public OrderResponseDTO getOrder(String orderId) {
        return orderDAO.getOrders(orderId);
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) throws Exception {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(UUID.randomUUID().toString());
        response.setName(orderRequestDTO.getName());
        response.setQty(orderRequestDTO.getQty());
        response.setPrice(orderRequestDTO.getPrice());
        response.setOrderDate(new Date());
        response.setStatus("CREATED");
        response.setEstimateDeliveryWindow(30);

        // Save order to database (same transaction)
        OrderResponseDTO savedOrder = orderDAO.saveOrder(response);

        // Write outbox event (same transaction as order)
        outboxEventService.createOutboxEvent(savedOrder);

        return savedOrder;
    }
}
