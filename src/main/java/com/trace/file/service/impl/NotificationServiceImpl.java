package com.trace.file.service.impl;

import org.springframework.stereotype.Service;
import com.trace.file.entity.Notification;
import com.trace.file.repository.NotificationRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive Notification Service
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 12:37:35
 */
@Service
public class NotificationServiceImpl {

    private final NotificationRepository notifRepo;

    public NotificationServiceImpl(NotificationRepository notifRepo) {
        this.notifRepo = notifRepo;
    }

    /**
     * Save notifications reactively in bulk.
     * Uses saveAll to avoid memory blow‑up and reduce DB round‑trips.
     */
    public Mono<Void> saveNotifications(Flux<Notification> notifications) {
        return notifRepo.saveAll(notifications) // returns Flux<Notification>
                        .then();                // convert to Mono<Void> (completion signal)
    }

    /**
     * Convenience overload if you already have a List.
     */
    public Mono<Void> saveNotifications(java.util.List<Notification> notifications) {
        return notifRepo.saveAll(Flux.fromIterable(notifications))
                        .then();
    }
}
