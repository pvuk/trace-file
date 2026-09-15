package com.trace.file.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trace.file.entity.FileUpload;
import com.trace.file.entity.Notification;
import com.trace.file.service.impl.EmailService;

import reactor.core.publisher.Flux;
/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Thursday 03-September-2026 15:26:22
 */
@RestController
@RequestMapping("/api/notify")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/notifyChecker")
    public void notifyChecker(@RequestBody FileUpload request) {
        emailService.sendNotifyEmail(request.getContactEmail(), request.getFileName(), request.getAssignedBy());
    }
    
    /**
     * Endpoint streams results reactively instead of blocking.
     * 
     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Tuesday 15-September-2026 18:05:27
     * @return
     */
//    @GetMapping("/maker/getAllNotifications")
//    public Flux<Notification> getAllNotifications() {
//        return service.getAllNotifications();
//    }
}
