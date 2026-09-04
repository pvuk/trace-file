package com.trace.file.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Friday 04-September-2026 14:18:40
 */
//@Component("auditorProvider")
public class SpringSecurityAuditorAware 
//implements AuditorAware<String> 
{

//    @Override
//    public Optional<String> getCurrentAuditor() {
//    	/*
//		 * Spring Data JPA will call this bean whenever it needs the current auditor.
//		 * You can fetch the username from Spring Security’s SecurityContextHolder.
//		 */
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return Optional.empty();
//        }
//
//        return Optional.ofNullable(authentication.getName()); 
//        // This will be the username (e.g., email or login ID)
//    }
}
