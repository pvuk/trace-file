package com.trace.file.bean;

import java.time.LocalDateTime;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;

import com.trace.file.entity.FileUpload;

import reactor.core.publisher.Mono;
/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 15-September-2026 17:44:04
 */
@Component
public class FileUploadAuditCallback implements BeforeConvertCallback<FileUpload> {
    @Override
    public Publisher<FileUpload> onBeforeConvert(FileUpload entity, SqlIdentifier table) {
        if (entity.getAssignDate() == null) {
            entity.setAssignDate(LocalDateTime.now());
        }
        return Mono.just(entity);
    }
}