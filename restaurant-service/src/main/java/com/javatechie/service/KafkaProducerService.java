package com.javatechie.service;

import com.javatechie.dto.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, OrderResponseDTO> kafkaTemplate;

    private static final String TOPIC = "order-topic";

    public void sendOrderEvent(OrderResponseDTO order) {
        kafkaTemplate.send(TOPIC, order.getOrderId(), order);
    }
}
