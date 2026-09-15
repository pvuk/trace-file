package com.trace.file.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Your entity must implement Persistable<ID> (this trips a lot of people up) — R2DBC needs an explicit way to know if a record is new (INSERT) or existing (UPDATE), since unlike JPA it doesn't track entity state:
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 12:00:41
 */
@Table(name = "NOTIFICATION")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends Auditable implements Persistable<Long> {
    @Id
    private Long id;
    private Long fileId;
    private String assignedTo;
    private String assignedBy;
    private boolean read;
    private LocalDateTime createdAt;
    
    @Transient
    private boolean isNew = true;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }

    // call this after loading from DB (e.g. in a @PostLoad-equivalent or in the repository layer)
    public void markNotNew() {
        this.isNew = false;
    }
}
