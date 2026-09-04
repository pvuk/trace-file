package com.trace.file.test.password.finder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.trace.file.design.factory.FormatterFactory;

/**
 * <b>Speed up the process</b></br>
 * 🔹 4. GPU acceleration
PDFBox itself is CPU‑only. To use GPU, you’d need specialized libraries or frameworks that offload cryptographic operations:

Hashcat: GPU‑accelerated password cracking tool (supports PDF).

John the Ripper: Another tool with GPU support for PDF.

Custom CUDA/OpenCL code: If you wanted to write your own GPU kernel for AES/RSA used in PDF encryption.

On a laptop, you can only use the GPU if it has CUDA (NVIDIA) or OpenCL support. Java doesn’t natively use GPU for PDFBox — you’d have to call out to these external tools.</br>

 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Sunday 23-August-2026 18:54:25
 */
public class PDFPasswordMultiThread_ParllelStream {
    public static void main(String[] args) throws InterruptedException, IOException {
    	
//    	Performance Tunning
//    	File pdfFile = new File("D:\\Backup\\Statement\\CC\\SBI Simply Click\\4726426850412133_02092016_PWD_4726426854430487.pdf");
//        if (!pdfFile.exists()) {
//            System.err.println("Error: PDF file not found!");
//            return;
//        }
//
//        // 🔹 Load file into memory once
//        byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//        
////      String prefix = "4";//finding 11 digits
//        String prefix = "4726";//4726 finding 8 digits (42685443)
////      String prefix = "47264268";//finding 4 digits (5443)
//        String suffix = "0487";
//        
//        int cores = Runtime.getRuntime().availableProcessors();//🔹 How many threads your CPU can handle
//        int streams = Math.max(1, cores - 3); // leave one core free. run parallel streams
//        log.info("Available cores: {}, Using Streams: {}", cores, streams);
////        System.out.println("Available cores: " + cores +", Total Streams: "+ streams);
//
//        
//        /*
//		 * if one stream checks ~19k passwords in 15 seconds, then running 5 parallel
//		 * streams should scale to ~100k passwords in the same time window. Let’s adapt
//		 * the multi‑threaded brute force so it uses parallel streams and reports
//		 * progress every 15 seconds.
//		 */
//        ExecutorService executor = Executors.newFixedThreadPool(streams);
//        
//        /*
//         * 🔹 General Formula
//				If you have n unknown digits, each digit can be 0–9 → 10 choices.
//				So total possibilities = 10𝑛.</br>
//				
//		   🔹 Key Notes
//				Always use long for large values (beyond 2,147,483,647).
//				
//				For 11 digits, the maximum is 99_999_999_999, but the count of possibilities is actually 100_000_000_000 (because you include 00000000000 as well).
//				
//				For n digits, the last number is all 9s (e.g., 9999 for 4 digits), but the total count is 10𝑛.</br>
//				
//         */
//		// 4 unknown digits → 10^4 = 10,000
////		long total = 10_000L;
////        int passwordFormatterLength = 4;
//
//		// 8 unknown digits → 10^8 = 100,000,000 = 100 million possibilities
//		long total = 100_000_000L;
//        int passwordFormatterLength = 8;
//
//		// 11 unknown digits → 10^11 = 99_999_999_999 + 1 = 100,000,000,000
////		long total = 100_000_000_000L;
////        int passwordFormatterLength = 11;
//
//		// 12 unknown digits → 10^12 = 1,000,000,000,000
////		long total = 1_000_000_000_000L;
////        int passwordFormatterLength = 12;
//        	
////        OR
////        ✅ So the declaration pattern is simply:
////        long total = (long) Math.pow(10, n);
//
//
//        long chunk = total / streams;
//
//        LongAdder counter = new LongAdder();
//        LongAdder[] counters = new LongAdder[streams];
//        
//        AtomicLong lastCandidate = new AtomicLong(-1);
//        AtomicLong[] lastCounters = new AtomicLong[streams]; // snapshot for throughput
//        for (int i = 0; i < streams; i++) {
//        	counters[i] = new LongAdder();
//            lastCounters[i] = new AtomicLong(0);
//        }
//
//        Instant startTime = Instant.now();
//
//        // Progress reporter every 15 seconds
//        ScheduledExecutorService reporter = Executors.newSingleThreadScheduledExecutor();
//        reporter.scheduleAtFixedRate(() -> {
//        	
////        	stopFlag ensures all threads stop cleanly once the password is found.
//        	if (stopFlag.get()) {
//        		reporter.shutdownNow(); // stop progress reporter
//        	    return; // exit thread
//        	}
//        	
//            long checked = counter.sum();
//            long remaining = total - checked;//shows how many records are left.
//            long last = lastCandidate.get();//last checked number
//            Duration elapsed = Duration.between(startTime, Instant.now());
//
//            // Calculate speed (records per second)
//            double speed = (checked > 0) ? (double) checked / elapsed.getSeconds() : 0.0;
//            long etaSeconds = (speed > 0) ? (long) (remaining / speed) : -1;//Estimated time remaining (ETA) based on current speed
//            
////          String lastCandidateChecked = (last >= 0 ? String.format("%011d", last) : "none");
////          String lastCandidateChecked = (last >= 0 ? String.format("%08d", last) : "none");
//            String lastCandidateChecked = (last >= 0 ? FormatterFactory.getFormatter(passwordFormatterLength).format(last) : "none");//Factory Design Pattern
////          String lastCandidateChecked = (last >= 0 ? String.format("%04d", last) : "none");
//            
//          String eta = (etaSeconds >= 0 ? formatElapsed(Duration.ofSeconds(etaSeconds)) : "N/A");
//
//          String formatElapsed = formatElapsed(elapsed);
//          
//            System.out.printf("[%s] Global Checked: %,d | Remaining to check: %,d | Last candidate: %s | Elapsed: %s | ETA: %s%n",
//                    LocalDateTime.now(),
//                    checked,
//                    remaining,
//                    lastCandidateChecked,
//                    formatElapsed,
//                    eta);
//            
//            publishGlobalStats(checked, remaining, lastCandidateChecked, formatElapsed, eta);
//            
////            for (int i = 0; i < streams; i++) {
////                System.out.printf("  Stream %d: %,d%n", i, counters[i].get());//👉 This way you’ll see per-stream contribution in addition to the global count.
////            }
//            
//            //Example 1: Print Group by 10
////            int[] streamsArray = new int[streams];
////            for (int i = 0; i < streamsArray.length; i++) {
////            	streamsArray[i] = (i % 30); // dummy values for demo
////            }
////            // Print grouped by 10
////            for (int i = 0; i < streamsArray.length; i++) {
////                System.out.printf("Stream %d: %d\t", i, streamsArray[i]);
////                if ((i + 1) % 10 == 0) {
////                    System.out.println(); // line break after every 10 streams
////                }
////            }
//            
//            //Example 2: Print Records / Sec processed by each stream
////            // Per-stream throughput
////            for (int i = 0; i < streams; i++) {
////                long current = counters[i].sum();
////                long prev = lastCounters[i].get();
////                long diff = current - prev;
////                lastCounters[i].set(current);
////
////                double streamSpeed = diff / 15.0; // reporter runs every 15s
////                double avgSpeed = (elapsed.getSeconds() > 0)
////                        ? (double) current / elapsed.getSeconds()
////                        : 0.0;
////
////                System.out.printf("  Stream %d: %,d total | +%,d in last 15s | ~%.2f rec/s (avg %.2f rec/s)%n",
////                        i, current, diff, streamSpeed, avgSpeed);
////                
////                Map<String, Object> payload = new HashMap<>();
////                payload.put("streamId", i);
////                payload.put("total", current);
////                payload.put("diff", "+"+ diff +" in last 15s");
//////                payload.put("streamSpeed", String.format("%.2f rec/s", streamSpeed));
////                payload.put("streamSpeed", streamSpeed);
////                payload.put("avgSpeed", avgSpeed);
////                sink.tryEmitNext(mapper.writeValueAsString(payload));
////            }
//            //Example 3: Throttle per-stream logs: once per minute
//            if (elapsed.getSeconds() % 60 == 0) {
//                for (int i = 0; i < streams; i++) {
//                    log.info("Stream {} total: {}", i, counters[i].sum());
//                }
//            }
//        }, 15, 15, TimeUnit.SECONDS);
//
//        for (int s = 0; s < streams; s++) {
//        	final int streamId = s;
//            long start = s * chunk;
//            long end = (s == streams - 1) ? total : (s + 1) * chunk;
//
//            executor.submit(() -> {
//                for (long i = start; i <= end; i++) {
////                    String middle = String.format("%011d", i);
//                    String middle = FormatterFactory.getFormatter(passwordFormatterLength).format(i);
//                    String candidate = prefix + middle + suffix;
//
//                    lastCandidate.set(i);
//                    counters[streamId].increment();// ✅ per-stream counter
//                    counter.increment();
//
//                    if (tryPassword(pdfBytes, candidate)) {
//                        System.out.println("Password found: " + candidate);
////                        You avoid System.exit(0), which is too abrupt and causes Spring’s lifecycle errors.
//                        //System.exit(0); // stop all streams immediately
//                        
//                        stopFlag.set(true);
//
////                        shutdownNow() interrupts running tasks, so they don’t keep looping.
//                        executor.shutdownNow();   // stop worker threads
////                        The reporter is also stopped, so no more progress events are emitted.
//                        reporter.shutdownNow();   // stop progress reporter
//                        
//                        stopScheduler();
//                        break;
//                    }
//                }
//            });
//        }
//
//        executor.shutdown();
//        /*
//         * Avoid blocking awaitTermination forever  
//			Since you’re shutting down manually, replace:
//         */
////        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//        executor.awaitTermination(30, TimeUnit.SECONDS);//This prevents the scheduler from hanging indefinitely.
//    
//	
    	
    	
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

        int streams = 90; // adjust based on CPU cores. run 5 / 90 parallel streams
        
        int cores = Runtime.getRuntime().availableProcessors();//🔹 How many threads your CPU can handle
        System.out.println("Available cores: " + cores +", Total Streams: "+ streams);

        
        /*
		 * if one stream checks ~19k passwords in 15 seconds, then running 5 parallel
		 * streams should scale to ~100k passwords in the same time window. Let’s adapt
		 * the multi‑threaded brute force so it uses parallel streams and reports
		 * progress every 15 seconds.
		 */
        ExecutorService executor = Executors.newFixedThreadPool(streams);

//      long total = 99_999_999_999L;// 11 digits
        long total = 100_000_000L; // 8 unknown digits = 100 million possibilities
//      long total = 9999L;// 4 digits;
        long chunk = total / streams;

        AtomicLong counter = new AtomicLong(0);
        AtomicLong lastCandidate = new AtomicLong(-1);
        
        AtomicLong[] counters = new AtomicLong[streams];
        AtomicLong[] lastCounters = new AtomicLong[streams]; // snapshot for throughput
        for (int i = 0; i < streams; i++) {
            counters[i] = new AtomicLong(0);
            lastCounters[i] = new AtomicLong(0);
        }

        Instant startTime = Instant.now();

        // Progress reporter every 15 seconds
        ScheduledExecutorService reporter = Executors.newSingleThreadScheduledExecutor();
        reporter.scheduleAtFixedRate(() -> {
            long checked = counter.get();
            long remaining = total - checked;//shows how many records are left.
            long last = lastCandidate.get();//last checked number
            Duration elapsed = Duration.between(startTime, Instant.now());

            // Calculate speed (records per second)
            double speed = (checked > 0) ? (double) checked / elapsed.getSeconds() : 0.0;
            long etaSeconds = (speed > 0) ? (long) (remaining / speed) : -1;//Estimated time remaining (ETA) based on current speed
            
            System.out.printf("[%s] Global Checked: %,d | Remaining to check: %,d | Last candidate: %s | Elapsed: %s | ETA: %s%n",
                    LocalDateTime.now(),
                    checked,
                    remaining,
//                  (last >= 0 ? String.format("%011d", last) : "none"),
                  (last >= 0 ? String.format("%08d", last) : "none"),
//                  (last >= 0 ? String.format("%04d", last) : "none"),
                    formatElapsed(elapsed),
                    (etaSeconds >= 0 ? formatElapsed(Duration.ofSeconds(etaSeconds)) : "N/A"));
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
            // Per-stream throughput
            for (int i = 0; i < streams; i++) {
                long current = counters[i].get();
                long prev = lastCounters[i].get();
                long diff = current - prev;
                lastCounters[i].set(current);

                double streamSpeed = diff / 15.0; // reporter runs every 15s
                double avgSpeed = (elapsed.getSeconds() > 0)
                        ? (double) current / elapsed.getSeconds()
                        : 0.0;

                System.out.printf("  Stream %d: %,d total | +%,d in last 15s | ~%.2f rec/s (avg %.2f rec/s)%n",
                        i, current, diff, streamSpeed, avgSpeed);
            }
        }, 15, 15, TimeUnit.SECONDS);

        for (int s = 0; s < streams; s++) {
        	final int streamId = s;
            long start = s * chunk;
            long end = (s == streams - 1) ? total : (s + 1) * chunk;

            executor.submit(() -> {
                for (long i = start; i <= end; i++) {
//                    String middle = String.format("%011d", i);
                    String middle = String.format("%08d", i);
                    String candidate = prefix + middle + suffix;

                    lastCandidate.set(i);
                    counters[streamId].incrementAndGet();// ✅ per-stream counter
                    counter.incrementAndGet();

                    if (tryPassword(pdfBytes, candidate)) {
                        System.out.println("Password found: " + candidate);
                        System.exit(0); // stop all streams immediately
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }
    
    /**
     * Try to open the PDF with the given password.
     * 
     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Monday 24-August-2026 13:11:55
     * @param pdfFile
     * @param password
     * @return
     */
    private static boolean tryPassword(File pdfFile, String password) {
        try (PDDocument doc = PDDocument.load(pdfFile, password)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * 🔹 Use byte[] instead of File
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
