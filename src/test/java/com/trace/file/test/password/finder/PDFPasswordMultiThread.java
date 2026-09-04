package com.trace.file.test.password.finder;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.pdfbox.pdmodel.PDDocument;
/**
 * 1. 🧵 Multi‑Threading (Fastest on a single machine)
	Use Java’s ExecutorService to run multiple threads in parallel. Each thread tries a subset of passwords.</br>
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Sunday 23-August-2026 16:20:24
 */
public class PDFPasswordMultiThread {
    public static void main(String[] args) throws InterruptedException {
        File pdfFile = new File("C:\\Users\\Priyanka\\Downloads\\4726426850412133_02092016.pdf");
        if (!pdfFile.exists()) {
            System.err.println("Error: PDF file not found!");
            return;
        }

//        String prefix = "4";//finding 11 digits
        String prefix = "4726";//4726 finding 8 digits (42685443)
//        String prefix = "47264268";//finding 4 digits (5443)
        String suffix = "0487";

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);

//        long total = 99_999_999_999L;// 11 digits
        long total = 99_999_999L;// 8 digits
//        long total = 9999L;// 4 digits;
        long chunk = total / threads;

        AtomicLong counter = new AtomicLong(0);//tracks total passwords tested across all threads.
        AtomicLong lastCandidate = new AtomicLong(-1);//stores the last attempted number.
        
        Instant startTime = Instant.now();
        
        // Progress reporter every 15 seconds
        ScheduledExecutorService reporter = Executors.newSingleThreadScheduledExecutor();
        reporter.scheduleAtFixedRate(() -> {
            long checked = counter.get();
            long last = lastCandidate.get();
            Duration elapsed = Duration.between(startTime, Instant.now());
            
            System.out.printf("[%s] Checked: %,d passwords. Last candidate: %s. Elapsed: %s%n",
                    LocalDateTime.now(),
                    checked,
//                    (last >= 0 ? String.format("%011d", last) : "none"),
                    (last >= 0 ? String.format("%08d", last) : "none"),
//                    (last >= 0 ? String.format("%04d", last) : "none"),
                    formatElapsed(elapsed));
        }, 15, 15, TimeUnit.SECONDS);

        for (int t = 0; t < threads; t++) {
            long start = t * chunk;
            long end = (t == threads - 1) ? total : (t + 1) * chunk;

            executor.submit(() -> {
                for (long i = start; i <= end; i++) {
//                    String middle = String.format("%011d", i);//11 digits
                	String middle = String.format("%08d", i);// 8 digits
//                    String middle = String.format("%04d", i);// 4 digits
                    String candidate = prefix + middle + suffix;

                    lastCandidate.set(i);
                    counter.incrementAndGet();

                    if (tryPassword(pdfFile, candidate)) {
                        System.out.println("Password found: " + candidate);
                        System.exit(0); // stop all threads immediately
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    private static boolean tryPassword(File pdfFile, String password) {
        try (PDDocument doc = PDDocument.load(pdfFile, password)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    // Format elapsed time in both styles
    private static String formatElapsed(Duration d) {
        long seconds = d.getSeconds();

        long years = seconds / (365L * 24 * 3600);
        seconds %= (365L * 24 * 3600);

        long months = seconds / (30L * 24 * 3600);
        seconds %= (30L * 24 * 3600);

        long weeks = seconds / (7L * 24 * 3600);
        seconds %= (7L * 24 * 3600);

        long days = seconds / (24 * 3600);
        seconds %= (24 * 3600);

        long hours = seconds / 3600;
        seconds %= 3600;

        long minutes = seconds / 60;
        seconds %= 60;

        // Detailed format
        String detailed = String.format("%dy:%dm:%dw:%dd:%02dH:%02dm:%02ds",
                years, months, weeks, days, hours, minutes, seconds);

        // Simple format
        String simple = String.format("%dd:%02dH:%02dm", days, hours, minutes);

        return detailed + " | " + simple;
    }
}
