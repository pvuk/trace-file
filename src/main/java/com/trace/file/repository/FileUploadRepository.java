package com.trace.file.repository;

import java.util.Optional;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.trace.file.entity.FileUpload;

/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 12:02:23
 */
@Repository
public interface FileUploadRepository extends ReactiveCrudRepository<FileUpload, Long> {

    Optional<FileUpload> findByIdempotencyKey(byte[] idempotencyKey);
}
