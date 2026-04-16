package com.javatechie.service;

import com.javatechie.dto.OrderResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;

    public KafkaConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-topic", groupId = "notification-group")
    public void consumeOrderEvent(String payload) throws Exception {
        String json = unwrapIfJsonString(payload);
        OrderResponseDTO order = objectMapper.readValue(json, OrderResponseDTO.class);
        System.out.println("Notification Service received order event: " + order);
        
        // Simulate Email Notification
        System.out.println("Sending Email Notification for Order: " + order.getOrderId());
        System.out.println("To: user@example.com");
        System.out.println("Subject: Order Confirmed - " + order.getName());
        System.out.println("Body: Your order for " + order.getQty() + " " + order.getName() + " has been placed successfully. Estimated delivery in " + order.getEstimateDeliveryWindow() + " mins.");
        
        // Simulate App Push Notification
        System.out.println("Sending App Push Notification: Your order " + order.getOrderId() + " is being prepared!");
        
        // Simulate Mobile/SMS Notification
        System.out.println("Sending SMS Notification to +1234567890: Order " + order.getOrderId() + " confirmed. Total: $" + order.getPrice());
        
        System.out.println("All notifications sent successfully for Order: " + order.getOrderId());
    }

    private String unwrapIfJsonString(String payload) throws Exception {
        if (payload == null) {
            return null;
        }
        String trimmed = payload.trim();
        if (trimmed.length() >= 2 && trimmed.charAt(0) == '"' && trimmed.charAt(trimmed.length() - 1) == '"') {
            // If the message is a JSON string (e.g. "\"{...}\""), decode the outer string first.
            return objectMapper.readValue(trimmed, String.class);
        }
        return trimmed;
    }
}
