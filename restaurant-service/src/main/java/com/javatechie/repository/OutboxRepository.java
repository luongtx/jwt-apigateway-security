package com.javatechie.repository;

import com.javatechie.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, String> {
    
}
