package com.javatechie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.dto.OrderResponseDTO;
import com.javatechie.entity.OutboxEvent;
import com.javatechie.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OutboxEventService {

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String AGGREGATE_TYPE_ORDER = "Order";
    private static final String EVENT_TYPE_ORDER_CREATED = "ORDER_CREATED";

    @Transactional
    public OutboxEvent createOutboxEvent(OrderResponseDTO orderResponse) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(orderResponse);

        OutboxEvent event = new OutboxEvent();
        event.setAggregateType(AGGREGATE_TYPE_ORDER);
        event.setEventType(EVENT_TYPE_ORDER_CREATED);
        event.setPayload(payload);
        event.setCreatedAt(LocalDateTime.now());
        event.setStatus(OutboxEvent.OutboxStatus.UNPUBLISHED);

        return outboxRepository.save(event);
    }
}
