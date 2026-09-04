package com.trace.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 18:48:01
 */
@RestController
//@RequestMapping("/api")
public class TraceFile_HealthController {
	
	@Autowired private JdbcTemplate jdbcTemplate;
	
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
	public ResponseEntity<String> dbHealthCheck() {
		try {
			jdbcTemplate.execute("SELECT 1 FROM DUAL");
			return ResponseEntity.ok("Database connection is healthy!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connection failed: " + e.getMessage());
		}
	}
}
