package com.javatechie.repository;

import com.javatechie.entity.OutboxEvent;
import com.javatechie.entity.OutboxEvent.OutboxStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, String> {

    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status, Pageable pageable);

    @Query("SELECT e FROM OutboxEvent e WHERE e.status = :status ORDER BY e.createdAt ASC")
    List<OutboxEvent> findUnpublishedEvents(@Param("status") OutboxStatus status, Pageable pageable);

    @Modifying
    @Query("UPDATE OutboxEvent e SET e.status = :newStatus, e.publishedAt = :publishedAt WHERE e.id IN :ids")
    int markAsPublished(@Param("ids") List<String> ids,
                        @Param("newStatus") OutboxStatus newStatus,
                        @Param("publishedAt") LocalDateTime publishedAt);

    @Modifying
    @Query("DELETE FROM OutboxEvent e WHERE e.status = :status AND e.publishedAt < :before")
    int deletePublishedEventsBefore(@Param("status") OutboxStatus status,
                                    @Param("before") LocalDateTime before);
}
