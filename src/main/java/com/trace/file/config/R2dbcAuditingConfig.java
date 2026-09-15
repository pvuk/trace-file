package com.trace.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import reactor.core.publisher.Mono;
/**
 * R2dbcAuditingConfig is a Spring configuration class that enables R2DBC auditing and provides a reactive auditor aware bean.
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 15-September-2026 17:35:09
 */
@Configuration
@EnableR2dbcAuditing
public class R2dbcAuditingConfig {

    @Bean
    public ReactiveAuditorAware<String> auditorProvider() {
//    	return () -> Mono.just(System.getProperty("user.name")); // Replace with logged-in user. TODO Check ReactiveSecurityContextHolder
    	
        // return the current user — e.g. pull from Spring Security's ReactiveSecurityContextHolder
        return () -> ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getName())
                .switchIfEmpty(Mono.just("system"));
    }
}