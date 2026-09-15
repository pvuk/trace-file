package com.trace.file.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trace.file.entity.FileUpload;
import com.trace.file.entity.Notification;
import com.trace.file.notification.reactive.NotificationPublisher;
import com.trace.file.service.impl.FileServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 12:04:32
 */
@RestController
@RequestMapping("/api")
public class FileController {
	private final Logger log = LoggerFactory.getLogger(FileController.class);
	
    @Autowired private FileServiceImpl service;
    @Autowired private NotificationPublisher publisher;
    
    @Autowired private R2dbcEntityTemplate r2dbcEntityTemplate;
    
    /**
     * New Request. Uploads file and metadata in one request.</br>
     * 
     * Code Reference: Interview:
     * 
     * The client must send a multipart/form-data request with both parts:

		One part for the file.
		
		One part for the JSON metadata (e.g., {"fileName":"abc.txt","assignedTo":"user1"}).</br>
		
		⚖️ When to Use</br>
			Use @RequestParam when:</br>
			
				You’re expecting form fields or query parameters.
				
				Example: ?page=1&size=10 or form-data: key=value.
			
			Use @RequestPart when:</br>
			
				You’re dealing with multipart/form-data requests that include files and/or JSON payloads.
				
				Example: form-data: file=<binary>, metadata=<json>.</br>
			
		🔑 Summary</br>
			@RequestParam → simple values (query/form fields)
			
			@RequestPart → multipart parts (files + JSON objects)</br>

     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Thursday 03-September-2026 12:32:07
     * @param idempotencyKey
     * @param filePart
     * @param fileUpload
     * @return
     */
//    @PostMapping("/files/uploadWithMetadata")
//    public Mono<ResponseEntity<Map<String, Object>>> uploadWithMetadata(
//            @RequestHeader("Idempotency-Key") UUID idempotencyKey,
//            @RequestPart("file") FilePart filePart,
//            @RequestPart("metadata") FileUpload fileUpload) {
// 
//    	String filename = filePart.filename();
//		if (filename == null || filename.isEmpty()) {
//			// Return an error response if the filename is missing or empty
//			return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(Map.of("error", "Please attach File. Filename is missing or empty")));
//		}
//    	
//        try {
//            Path uploadDir = Paths.get("C:/uploads");
//
//            if (!Files.exists(uploadDir)) {
//                Files.createDirectories(uploadDir);
//            }
//
//            Path target = uploadDir.resolve(filename);
//
//            // Save file asynchronously, then metadata
//            return filePart.transferTo(target)
//                    .then(Mono.fromSupplier(() -> {
//                        String metadataResult = null;
//                        
//                        // Save file record
//                    	fileUpload.setFileName(filename);
//                    	fileUpload.setFilePath(uploadDir.toFile().getPath());
//                        fileUpload.setIdempotencyKey(idempotencyKey);
//                        Mono<Long> monoFile = service.saveFile(fileUpload);
//                        
//						monoFile.subscribe(fileId -> {
//							// After saving the file, save metadata
//							fileUpload.setId(fileId);
//							metadataResult = service.updateFile(fileUpload);
//						});
//                        Long fileId = fileUpload.getId();
//
//                        Map<String, Object> response = Map.of(
//                                "message", "File and metadata saved successfully",
//                                "fileId", fileId.toString(),
//                                "metadataResult", metadataResult,
//                                "savedFile", fileUpload
//                        );
//                        return ResponseEntity.ok(response);
//                    }))
//                    .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                            .body(Map.of("error", "Error saving file/metadata: " + e.getMessage()))));
//
//        } catch (Exception e) {
//            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Error: " + e.getMessage())));
//        }
//    }

    /**
     * Old Request. Deprecated.
     * 
     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Thursday 03-September-2026 12:31:40
     * @param idempotencyKey
     * @param filePart
     * @return
     */
//    @PostMapping("/files/uploadFile")
//	public Mono<ResponseEntity<String>> uploadFile(@RequestHeader("Idempotency-Key") UUID idempotencyKey,
//			@RequestPart("file") FilePart filePart) {
//    	
//        try {
//            Path uploadDir = Paths.get("C:/uploads");
//            
//            if (!Files.exists(uploadDir)) {
//                Files.createDirectories(uploadDir);
//            }
//
//            Path target = uploadDir.resolve(filePart.filename());
//
////            return filePart.transferTo(target)
////                    .then(Mono.just(ResponseEntity.ok("File saved to: " + target.toString())));
//         // Save file asynchronously
//            return filePart.transferTo(target)
//            		.then(Mono.fromSupplier(() -> {
//                        Long result = service.saveFile(filePart.filename(), uploadDir.toFile().getPath());
//                        return ResponseEntity.ok(result.toString());
//                    }))
//                    .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                            .body("Error saving file: " + e.getMessage())));
//        } catch (Exception e) {
//            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error: " + e.getMessage()));
//        }
//    }

//    @PostMapping("/files/saveMetadata")
//    public Mono<ResponseEntity<Map<String, String>>> saveMetadata(@RequestBody FileUpload fileUpload) {
//        try {
////        	if (service.existsByFileNameAndAssignedTo(fileUpload.getFileName(), fileUpload.getAssignedTo())) {
////        	    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
////        	        .body(Map.of("error", "Duplicate assignment")));
////        	}
//
//            String result = service.updateFile(fileUpload);
////            return Mono.just(ResponseEntity.ok("Metadata saved: " + result));
//            Map<String, String> response = Map.of("message", "Metadata saved", "result", result);
//            return Mono.just(ResponseEntity.ok(response));
//        } catch (Exception e) {
////            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////                    .body("Error saving metadata: " + e.getMessage()));
//        	Map<String, String> error = Map.of("error", "Error saving metadata: " + e.getMessage());
//            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
//        }
//    }

    
    @GetMapping("/checker/notifications/history/{checker}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable String checker) {
        return ResponseEntity.ok(service.getUnreadNotifications(checker));
    }

    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id) {
        service.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    // SSE stream for auto-refresh
    @GetMapping(value = "/checker/notifications/stream/{checker}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Notification> streamNotifications(@PathVariable String checker) {
//        return Flux.interval(Duration.ofSeconds(5))
//                   .flatMap(seq -> Flux.fromIterable(service.getUnreadNotifications(checker)));
    	log.info("Checker subscribed to stream");//→ Confirms Checker connected.
        return publisher.getStream();
    }
    
    /**
     * 🎯 Interview Soundbite
	"Partitioning splits data into chunks so queries only scan relevant partitions. But with 100M/day, the active table will explode. That’s why we combine partitioning with archiving: keep recent partitions in the active table for speed, and move old partitions into archive tables monthly using DBMS_SCHEDULER. This way, queries on recent data are lightning fast, and old data is still accessible when needed."

     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Tuesday 25-August-2026 14:06:50
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/checker/notifications/archive/search")
    public Flux<Notification> searchArchived(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {

    	return r2dbcEntityTemplate.getDatabaseClient()
            .sql("SELECT * FROM notification_archive WHERE created_at BETWEEN :start AND :end")
            .bind("start", start)
            .bind("end", end)
            .map((row, metadata) -> Notification.builder()
                .id(row.get("id", Long.class))
                .fileId(row.get("file_id", Long.class))
                .assignedTo(row.get("assigned_to", String.class))
                .assignedBy(row.get("assigned_by", String.class))
                .read(row.get("read", Boolean.class))
                .createdAt(row.get("created_at", LocalDateTime.class))
                .build()
            )
            .all();   // ✅ correct terminal operator
    }
    
    /**
     * Endpoint streams results reactively instead of blocking.
     * 
     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Tuesday 15-September-2026 17:58:35
     * @return
     */
    @GetMapping("/maker/getAllFiles")
    public Mono<List<FileUpload>> getAllFiles() {
        return service.getAllFiles();
    }

}
