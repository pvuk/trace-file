package com.trace.file.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
/**
 * ErrorController is a Spring Boot REST controller that simulates an error for testing purposes.
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 15-September-2026 15:55:48
 */
@RestController
@RequestMapping("/error-reproduce")
public class ErrorController {

    @GetMapping("/simulate-error")
    public Mono<String> simulateError() {
        return Mono.error(new RuntimeException("Simulated SSE failure for tracing"));
    }
}
