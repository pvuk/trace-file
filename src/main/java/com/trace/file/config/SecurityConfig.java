package com.trace.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Wednesday 26-August-2026 15:22:34
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    	http
        .cors(corsSpec -> {})   // enable CORS with a no-op customizer
//        .csrf(csrfSpec -> {})   // disable CSRF with a no-op customizer
//        .authorizeExchange(exchangeSpec -> 
//            exchangeSpec
//                .pathMatchers("/api/**").permitAll()
//                .anyExchange().authenticated()
        //OR
//        Quick Development Setup (disable security)
//        If you don’t want login at all while developing:
          .csrf(csrfSpec -> csrfSpec.disable())
          .authorizeExchange(exchange -> exchange.anyExchange().permitAll() //👉 This allows all requests without authentication.
        );
    	return http.build();
    }
}
