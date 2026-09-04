package com.trace.file.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 12:00:41
 */
@Entity
@Table(name = "NOTIFICATION")
@Data
public class Notification {
    @Id @GeneratedValue
    private Long id;
    private Long fileId;
    private String assignedTo;
    private String assignedBy;
    private boolean read;
    private LocalDateTime createdAt;
}
