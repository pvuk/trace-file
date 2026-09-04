package com.trace.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Code Reference: Interview:
 * 
 * Spring’s TaskScheduler lets you manage scheduled tasks programmatically.

	Add a field: @Autowired private TaskScheduler taskScheduler;
	
	This gives you control over scheduled jobs.

 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 01-September-2026 21:37:23
 */
@Configuration
public class SchedulerConfig {
    @Bean
    public TaskScheduler taskScheduler() {
    	//deprecated
//        return new ConcurrentTaskScheduler(); // wraps ScheduledExecutorService
    	//OR
    	ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5); // configure pool size
        scheduler.setThreadNamePrefix("trace-scheduler-");
        scheduler.initialize();
        return scheduler;
    }
}
