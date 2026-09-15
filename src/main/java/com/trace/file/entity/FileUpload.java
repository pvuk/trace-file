package com.trace.file.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 11:59:30
 */
@Table("FILE_UPLOAD")
// Unique constraint on idempotencyKey is no longer declared here —
// R2DBC does not generate/alter schema. Create it in Oracle directly
// or via a Flyway/Liquibase migration:
// ALTER TABLE FILE_UPLOAD ADD CONSTRAINT uq_file_upload_idempotency_key UNIQUE (IDEMPOTENCY_KEY);
@Data
@EqualsAndHashCode(callSuper = false)
public class FileUpload extends Auditable implements Persistable<Long>{

    @Id
    private Long id; // fileId
    private String fileName;
    private String filePath;
    private String fileData;

    @Column("ASSIGNED_BY")
    private String assignedBy;

    @Column("ASSIGNED_TO")
    private String assignedTo;

    private LocalDateTime assignDate;
    private String fullName;
    private LocalDate dateOfBirth;
    private Long cardNumber;
    private String passwordHint;
    private String contactEmail;
    private String passwordTracked;

    private byte[] idempotencyKey;
    
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