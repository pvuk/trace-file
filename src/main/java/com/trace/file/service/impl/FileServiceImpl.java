package com.trace.file.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.trace.file.entity.FileUpload;
import com.trace.file.entity.Notification;
import com.trace.file.notification.reactive.NotificationPublisher;
import com.trace.file.repository.FileUploadRepository;
import com.trace.file.repository.NotificationRepository;
/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 12:03:31
 */
@Service
public class FileServiceImpl {
	private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
	
    private final NotificationPublisher publisher;
    
    private final FileUploadRepository fileRepo;
    private final NotificationRepository notifRepo;
    
	public FileServiceImpl(FileUploadRepository fileRepo, NotificationRepository notifRepo,
			NotificationPublisher publisher) {
		this.fileRepo = fileRepo;
		this.notifRepo = notifRepo;
		this.publisher = publisher;
	}

    public Long saveFile(FileUpload file) {
        FileUpload saved = fileRepo.save(file);
        return saved.getId();
    }
    
    public String updateFile(FileUpload file) {
		UUID idempotencyKey = file.getIdempotencyKey();
    	Optional<FileUpload> existing = fileRepo.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            return "File already exist."; // return same response if retried
        }
        
    	Long fileID = file.getId();
//		FileUpload saved = fileRepo.findById(file.getId()).orElseThrow(() -> new RuntimeException("File not found"));
//		saved.setContactEmail(file.getContactEmail());
//		fileRepo.save(saved);

        Notification notif = new Notification();
        notif.setFileId(fileID);
        notif.setAssignedTo(file.getAssignedTo());
        notif.setAssignedBy(file.getAssignedBy());
        notif.setRead(false);
        notif.setCreatedAt(LocalDateTime.now());
        notifRepo.save(notif);

        publisher.publish(notif);  // ✅ Live stream (SSE/WebFlux) → uses ReplayProcessor
        
		return "File Metadata Saved Successfully with ID: " + fileID;
    }

    public List<Notification> getUnreadNotifications(String checker) {
        return notifRepo.findByAssignedToAndReadFalse(checker);
    }

    public void markAsRead(Long notifId) {
        notifRepo.findById(notifId).ifPresent(n -> {
            n.setRead(true);
            notifRepo.save(n);
        });
    }

	public boolean existsByFileNameAndAssignedTo(String fileName, String assignedTo) {
		
		return false;
	}
	

    public List<FileUpload> getAllFiles() {
        return fileRepo.findAll();
    }
}
