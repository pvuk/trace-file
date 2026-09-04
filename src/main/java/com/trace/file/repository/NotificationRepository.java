package com.trace.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trace.file.entity.Notification;

/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 12:01:56
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAssignedToAndReadFalse(String assignedTo);
    
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
