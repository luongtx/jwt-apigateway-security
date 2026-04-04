package com.javatechie.service;

import com.javatechie.dto.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, OrderResponseDTO> kafkaTemplate;

    private static final String TOPIC = "order-topic";

    public void sendOrderEvent(OrderResponseDTO order) {
        String orderId = Objects.requireNonNull(order.getOrderId(), "OrderId cannot be null");
        kafkaTemplate.send(TOPIC, orderId, order);
    }
}
