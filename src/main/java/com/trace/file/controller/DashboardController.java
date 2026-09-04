package com.trace.file.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trace.file.design.factory.FormatterFactory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import tools.jackson.databind.ObjectMapper;

/**
 * You can cancel the Spring @Scheduled task itself once the password is found, 
 * so it doesn’t run again on the next cron tick. By default, Spring’s scheduler will keep invoking your method at the configured cron expression. To stop it permanently after success,
 * you need to manage the scheduled future yourself.</br>
 * 
 * 🚀 Usage Flow
		User calls /dashboard/start → scheduler begins running every 15s.
		
		When password is found → stopScheduler() is invoked automatically.
		
		User can later call /dashboard/start again to create a new scheduler.

 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 27-August-2026 18:44:59
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	private Logger log = LoggerFactory.getLogger(DashboardController.class);

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final AtomicBoolean stopFlag = new AtomicBoolean(false);
    private static final ObjectMapper mapper = new ObjectMapper(); // shared instance
    
    private final TaskScheduler scheduler;
    private ScheduledFuture<?> currentTask;
    
	public DashboardController(TaskScheduler scheduler) {
		this.scheduler = scheduler;
	}
    
    /**
     * 🔎 Why it feels like “continuous calls”
		SSE protocol: The browser keeps the connection alive and retries if it closes.
		
		Flux sink: Even with no tryEmitNext(...), the stream remains active, so the client sees ongoing activity.
		
		Logging: You’re logging the Flux object, not the emitted data.

     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Friday 28-August-2026 16:38:10
     * @return
     */
    @GetMapping(value = "/progress", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamProgress() {
    	/*
		 * When you return sink.asFlux() directly, the Flux is hot and stays open. Even
		 * if you don’t publish anything, the SSE connection itself keeps sending
		 * heartbeat frames (empty events) to keep the stream alive. That’s why your
		 * client sees continuous calls.
		 * 
		 */
//    	Flux<String> asFlux = sink.asFlux();
//    	log.info("Streaming progress updates to clients: ", asFlux);

    	//    	OR
    	
    	/*
    	 * 🔎 Why logs don’t appear
				sink.asFlux() is a hot publisher, but it only reacts when something is pushed into the sink.
				
				Your SSE connection itself stays alive (so the client keeps receiving “Received SSE data”), but those are heartbeat frames from the browser/SSE protocol, not actual events from your sink.
				
				Since you never call sink.tryEmitNext("some payload"), the doOnNext(...) hook never fires → no “Publishing Event” log.
    	 */
    	Flux<String> asFlux = sink.asFlux().doOnNext(event -> log.info("Publishing Event: " + event));//This way you’ll only see logs when something is actually emitted.
        return asFlux;
    }

    // Call this from your reporter instead of System.out.println
    public void publish(String message) {
        sink.tryEmitNext(message);
    }
    
    /**
     * 🔑 Key Points
			TaskScheduler.scheduleAtFixedRate lets you start jobs dynamically.
			
			ScheduledFuture.cancel(true) stops the job immediately.
			
			You can expose REST endpoints (/start, /stop) so the user can control scheduling.
			
			No need to restart Tomcat/Netty — jobs are managed in memory.

     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Tuesday 01-September-2026 21:44:11
     * @return
     */
    @PostMapping("/start")
    public String startScheduler() {
        if (currentTask != null && !currentTask.isCancelled()) {
            return "Scheduler already running!";
        }
        stopFlag.set(false);
        
        currentTask = scheduler.scheduleAtFixedRate(() -> {
			try {
				tracePassword();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, Duration.ofSeconds(15));
        return "Scheduler started.";
    }
    
    /**
     * Code Reference: Interview:
     * 
     * Keep the returned ScheduledFuture<?> reference so you can call .cancel(true) when the password is found.

	 * Later, if the user wants another run, just call scheduleAtFixedRate again — no Tomcat/Netty restart needed.

     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Tuesday 01-September-2026 21:55:52
     * @return
     */
    @PostMapping("/stop")
    public String stopScheduler() {
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
            stopFlag.set(true);
            return "Scheduler stopped.";
        }
        return "No scheduler running.";
    }

//	@Scheduled(cron = "30 49 17 * * ?")
	public void tracePassword() throws InterruptedException, IOException {


    	File pdfFile = new File("D:\\Backup\\Statement\\CC\\SBI Simply Click\\4726426850412133_02092016_PWD_4726426854430487.pdf");
        if (!pdfFile.exists()) {
            System.err.println("Error: PDF file not found!");
            return;
        }

        // 🔹 Load file into memory once
        byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
        
//      String prefix = "4";//finding 11 digits
        String prefix = "4726";//4726 finding 8 digits (42685443)
//      String prefix = "47264268";//finding 4 digits (5443)
        String suffix = "0487";
        
        int cores = Runtime.getRuntime().availableProcessors();//🔹 How many threads your CPU can handle
        int streams = Math.max(1, cores - 3); // leave one core free. run parallel streams
        log.info("Available cores: {}, Using Streams: {}", cores, streams);
//        System.out.println("Available cores: " + cores +", Total Streams: "+ streams);

        
        /*
		 * if one stream checks ~19k passwords in 15 seconds, then running 5 parallel
		 * streams should scale to ~100k passwords in the same time window. Let’s adapt
		 * the multi‑threaded brute force so it uses parallel streams and reports
		 * progress every 15 seconds.
		 */
        ExecutorService executor = Executors.newFixedThreadPool(streams);
        
        /*
         * 🔹 General Formula
				If you have n unknown digits, each digit can be 0–9 → 10 choices.
				So total possibilities = 10𝑛.</br>
				
		   🔹 Key Notes
				Always use long for large values (beyond 2,147,483,647).
				
				For 11 digits, the maximum is 99_999_999_999, but the count of possibilities is actually 100_000_000_000 (because you include 00000000000 as well).
				
				For n digits, the last number is all 9s (e.g., 9999 for 4 digits), but the total count is 10𝑛.</br>
				
         */
		// 4 unknown digits → 10^4 = 10,000
//		long total = 10_000L;
//        int passwordFormatterLength = 4;

		// 8 unknown digits → 10^8 = 100,000,000 = 100 million possibilities
		long total = 100_000_000L;
        int passwordFormatterLength = 8;

		// 11 unknown digits → 10^11 = 99_999_999_999 + 1 = 100,000,000,000
//		long total = 100_000_000_000L;
//        int passwordFormatterLength = 11;

		// 12 unknown digits → 10^12 = 1,000,000,000,000
//		long total = 1_000_000_000_000L;
//        int passwordFormatterLength = 12;
        	
//        OR
//        ✅ So the declaration pattern is simply:
//        long total = (long) Math.pow(10, n);


        long chunk = total / streams;

        LongAdder counter = new LongAdder();
        LongAdder[] counters = new LongAdder[streams];
        
        AtomicLong lastCandidate = new AtomicLong(-1);
        AtomicLong[] lastCounters = new AtomicLong[streams]; // snapshot for throughput
        for (int i = 0; i < streams; i++) {
        	counters[i] = new LongAdder();
            lastCounters[i] = new AtomicLong(0);
        }

        Instant startTime = Instant.now();

        // Progress reporter every 15 seconds
        ScheduledExecutorService reporter = Executors.newSingleThreadScheduledExecutor();
        reporter.scheduleAtFixedRate(() -> {
        	
//        	stopFlag ensures all threads stop cleanly once the password is found.
        	if (stopFlag.get()) {
        		reporter.shutdownNow(); // stop progress reporter
        	    return; // exit thread
        	}
        	
            long checked = counter.sum();
            long remaining = total - checked;//shows how many records are left.
            long last = lastCandidate.get();//last checked number
            Duration elapsed = Duration.between(startTime, Instant.now());

            // Calculate speed (records per second)
            double speed = (checked > 0) ? (double) checked / elapsed.getSeconds() : 0.0;
            long etaSeconds = (speed > 0) ? (long) (remaining / speed) : -1;//Estimated time remaining (ETA) based on current speed
            
//          String lastCandidateChecked = (last >= 0 ? String.format("%011d", last) : "none");
//          String lastCandidateChecked = (last >= 0 ? String.format("%08d", last) : "none");
            String lastCandidateChecked = (last >= 0 ? FormatterFactory.getFormatter(passwordFormatterLength).format(last) : "none");//Factory Design Pattern
//          String lastCandidateChecked = (last >= 0 ? String.format("%04d", last) : "none");
            
          String eta = (etaSeconds >= 0 ? formatElapsed(Duration.ofSeconds(etaSeconds)) : "N/A");

          String formatElapsed = formatElapsed(elapsed);
          
            System.out.printf("[%s] Global Checked: %,d | Remaining to check: %,d | Last candidate: %s | Elapsed: %s | ETA: %s%n",
                    LocalDateTime.now(),
                    checked,
                    remaining,
                    lastCandidateChecked,
                    formatElapsed,
                    eta);
            
            publishGlobalStats(checked, remaining, lastCandidateChecked, formatElapsed, eta);
            
//            for (int i = 0; i < streams; i++) {
//                System.out.printf("  Stream %d: %,d%n", i, counters[i].get());//👉 This way you’ll see per-stream contribution in addition to the global count.
//            }
            
            //Example 1: Print Group by 10
//            int[] streamsArray = new int[streams];
//            for (int i = 0; i < streamsArray.length; i++) {
//            	streamsArray[i] = (i % 30); // dummy values for demo
//            }
//            // Print grouped by 10
//            for (int i = 0; i < streamsArray.length; i++) {
//                System.out.printf("Stream %d: %d\t", i, streamsArray[i]);
//                if ((i + 1) % 10 == 0) {
//                    System.out.println(); // line break after every 10 streams
//                }
//            }
            
            //Example 2: Print Records / Sec processed by each stream
//            // Per-stream throughput
//            for (int i = 0; i < streams; i++) {
//                long current = counters[i].sum();
//                long prev = lastCounters[i].get();
//                long diff = current - prev;
//                lastCounters[i].set(current);
//
//                double streamSpeed = diff / 15.0; // reporter runs every 15s
//                double avgSpeed = (elapsed.getSeconds() > 0)
//                        ? (double) current / elapsed.getSeconds()
//                        : 0.0;
//
//                System.out.printf("  Stream %d: %,d total | +%,d in last 15s | ~%.2f rec/s (avg %.2f rec/s)%n",
//                        i, current, diff, streamSpeed, avgSpeed);
//                
//                Map<String, Object> payload = new HashMap<>();
//                payload.put("streamId", i);
//                payload.put("total", current);
//                payload.put("diff", "+"+ diff +" in last 15s");
////                payload.put("streamSpeed", String.format("%.2f rec/s", streamSpeed));
//                payload.put("streamSpeed", streamSpeed);
//                payload.put("avgSpeed", avgSpeed);
//                sink.tryEmitNext(mapper.writeValueAsString(payload));
//            }
            //Example 3: Throttle per-stream logs: once per minute
            if (elapsed.getSeconds() % 60 == 0) {
                for (int i = 0; i < streams; i++) {
                    log.info("Stream {} total: {}", i, counters[i].sum());
                }
            }
        }, 15, 15, TimeUnit.SECONDS);

        for (int s = 0; s < streams; s++) {
        	final int streamId = s;
            long start = s * chunk;
            long end = (s == streams - 1) ? total : (s + 1) * chunk;

            executor.submit(() -> {
                for (long i = start; i <= end; i++) {
//                    String middle = String.format("%011d", i);
                    String middle = FormatterFactory.getFormatter(passwordFormatterLength).format(i);
                    String candidate = prefix + middle + suffix;

                    lastCandidate.set(i);
                    counters[streamId].increment();// ✅ per-stream counter
                    counter.increment();

                    if (tryPassword(pdfBytes, candidate)) {
                        System.out.println("Password found: " + candidate);
//                        You avoid System.exit(0), which is too abrupt and causes Spring’s lifecycle errors.
                        //System.exit(0); // stop all streams immediately
                        
                        stopFlag.set(true);

//                        shutdownNow() interrupts running tasks, so they don’t keep looping.
                        executor.shutdownNow();   // stop worker threads
//                        The reporter is also stopped, so no more progress events are emitted.
                        reporter.shutdownNow();   // stop progress reporter
                        
                        stopScheduler();
                        break;
                    }
                }
            });
        }

        executor.shutdown();
        /*
         * Avoid blocking awaitTermination forever  
			Since you’re shutting down manually, replace:
         */
//        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        executor.awaitTermination(30, TimeUnit.SECONDS);//This prevents the scheduler from hanging indefinitely.
    
	}
    
    public void publishGlobalStats(long checked, long remaining, String lastCandidate,
            String elapsed, String eta) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("type", "global");
		payload.put("checked", checked);
		payload.put("remaining", remaining);
		payload.put("lastCandidate", lastCandidate);
		payload.put("elapsed", elapsed);
		payload.put("eta", eta);
		
//		sink.tryEmitNext(mapper.writeValueAsString(payload));
		
		//OR
		/*
		 * If ObjectMapper.writeValueAsString(...) throws, the event never gets emitted. Wrap this in a try/catch and log errors:
		 */
//		ObjectMapper mapper = new ObjectMapper();
		try {
		    sink.tryEmitNext(mapper.writeValueAsString(payload));
		} catch (Exception e) {
		    log.error("Failed to serialize payload", e);
		}

	}
    
    /**
     * Code Ref: Interview: How try-with-resources works</br>
     * 
     * 🔹 Use byte[] instead of File. It reads the PDF into memory once, avoiding repeated disk I/O for each password attempt. This drastically improves performance in brute-force scenarios. </br>
     * 
     * PDDocument.load(...)  </br>
		This attempts to open the PDF using the candidate password. If the password is correct, it returns a PDDocument object.</br>
	
		try-with-resources  </br>
			The parentheses after try (try (PDDocument doc = ...)) declare a resource that implements AutoCloseable.
			
			Here, PDDocument implements Closeable.
			
			When the try block finishes (whether normally or due to an exception), Java automatically calls doc.close().
			
			This ensures the file handle and memory are released immediately, without needing a finally block.</br>
		
		Return true/false</br>
		
			If the PDF opens successfully → return true.
			
			If an IOException is thrown (wrong password or corrupted file) → catch it and return false.</br>

     * Normally, you’d see:</br>

		PDDocument doc = null;
		try {
		    doc = PDDocument.load(pdfBytes, password);
		    return true;
		} catch (IOException e) {
		    return false;
		} finally {
		    if (doc != null) {
		        doc.close();
		    }
		}
		But that’s verbose. The try‑with‑resources version is cleaner:
		
		No need for a finally block.
		
		No risk of forgetting to close the document.
		
		Less boilerplate, more readable.
		
		Automatic resource management → safer under heavy brute‑force loops.</br>


     * 🚀 Takeaway</br>
		They wrote try (PDDocument doc = ...) instead of declaring doc outside because:
		
		It guarantees automatic cleanup of the PDF resource.
		
		It avoids manual close() calls.
		
		It keeps the code short and safe, especially when millions of attempts are being made.</br>
     * 
     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Monday 24-August-2026 13:12:19
     * @param pdfBytes
     * @param password
     * @return
     */
    private static boolean tryPassword(byte[] pdfBytes, String password) {
        try (PDDocument doc = PDDocument.load(pdfBytes, password)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Format elapsed time in days:hours:minutes:seconds
    private static String formatElapsed(Duration d) {
        long seconds = d.getSeconds();
        long days = seconds / (24 * 3600);
        seconds %= (24 * 3600);
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        return String.format("%dd:%02dH:%02dm:%02ds", days, hours, minutes, seconds);
    }
}
