package com.trace.file.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

public abstract class Auditable {
	@CreatedBy
	@Column("CREATED_BY")
	private String createdBy;

	@CreatedDate
	@Column("CREATED_ON")
	private LocalDateTime createdOn;
	
	@LastModifiedBy
	@Column("UPDATED_BY")
	private String updatedBy;
	
	@LastModifiedDate
	@Column("UDPATED_ON")
	private LocalDateTime udpatedOn;
}
