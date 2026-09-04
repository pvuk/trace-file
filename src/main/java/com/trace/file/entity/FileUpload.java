package com.trace.file.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 11:59:30
 */
@Entity
//Use @Table(uniqueConstraints = contactEmail + idempotencyKey) for composite uniqueness.
@Table(name="FILE_UPLOAD",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"idempotencyKey"})
    })//Unique Constraint at Table Level
@Data
@EqualsAndHashCode(callSuper=false)
public class FileUpload extends Auditable{
    @Id @GeneratedValue
    private Long id;//fileId
    private String fileName;
    private String filePath;
    private String fileData;
    @Column(name = "ASSIGNED_BY")
    private String assignedBy;
    @Column(name = "ASSIGNED_TO")
    private String assignedTo;
//    private LocalDate assignDate;
    private LocalDateTime assignDate;
    private String fullName;
    private LocalDate dateOfBirth;
    private Long cardNumber;
    private String passwordHint;
    private String contactEmail;//Password will be sent once found
    private String passwordTracked;//Once tracked save password in DashboardController > tracePassword method
    
    //Unique Constraint at Column Level
//    @Column(unique = true, nullable = false)
    private UUID idempotencyKey;
    
    /**
     * Code Reference: Interview:
     * Keeps logic inside the entity (no need for triggers or defaults in SQL).</br>
     * 
     * 🔧 Lifecycle Hook Behavior</br>
		@PrePersist is a JPA callback annotation.
		
		When you call entityManager.persist(entity) or repository.save(entity) for a new entity:
		
		Hibernate prepares the insert.
		
		It automatically calls your onCreate() method.
		
		The assignDate field gets set before the SQL INSERT executes.
		
		You never call onCreate() manually — it’s triggered by the persistence provider.</br>
     */
 // --- Lifecycle hook ---
    @PrePersist
    protected void onCreate() {
        if (assignDate == null) {
            assignDate = LocalDateTime.now();
        }
    }
}
