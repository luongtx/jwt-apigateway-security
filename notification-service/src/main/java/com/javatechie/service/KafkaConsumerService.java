package com.javatechie.service;

import com.javatechie.dto.OrderResponseDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "order-topic", groupId = "notification-group")
    public void consumeOrderEvent(OrderResponseDTO order) {
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
}
