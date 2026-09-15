package com.trace.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 18:48:01
 */
@RestController
//@RequestMapping("/api")
public class TraceFile_HealthController {
	
	@Autowired private R2dbcEntityTemplate r2dbcEntityTemplate;
	
	/**
	 * Basic health check endpoint to verify if the Trace File Service is up and running.
	 * 
	 * @author PULIPATI VENKATA UDAYKIRAN
	 * @since Tuesday 25-August-2026 18:49:26
	 * @return
	 */
	@GetMapping("/health")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("Trace File Service is healthy!");
	}
	
	@GetMapping("/db-health")
	public Mono<ResponseEntity<String>> dbHealthCheck() {
	    return r2dbcEntityTemplate.getDatabaseClient()
	        .sql("SELECT 1 FROM DUAL")
	        .fetch()
	        .one()
	        .map(row -> ResponseEntity.ok("Database connection is healthy!"))
	        .onErrorResume(e ->
	            Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	                .body("Database connection failed: " + e.getMessage()))
	        );
	}

}
