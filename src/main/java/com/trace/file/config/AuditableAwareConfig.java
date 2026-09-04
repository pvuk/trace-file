package com.trace.file.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Friday 04-September-2026 14:05:34
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")//Enable JPA Auditing
public class AuditableAwareConfig {
	
	@Bean
	public AuditorAware<String> auditorProvider() {
	    return () -> Optional.of(System.getProperty("user.name")); // Replace with logged-in user. TODO Check SpringSecurityAuditorAware.java
	}
}
