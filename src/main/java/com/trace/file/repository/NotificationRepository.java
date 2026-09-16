package com.trace.file.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.trace.file.entity.Notification;

import reactor.core.publisher.Flux;

/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 12:01:56
 */
@Repository
public interface NotificationRepository extends ReactiveCrudRepository<Notification, Long> {
    
	Flux<Notification> findByAssignedToAndReadFalse(String assignedTo);
    
    /**
     * Bulk Operations
		Instead of looping in Java:
		👉 Push bulk updates/deletes to DB. Avoid row‑by‑row processing.

     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Tuesday 25-August-2026 12:42:54
     * @param checker
     */
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.assignedTo = :checker")
    void markAllRead(@Param("checker") String checker);

}
