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
 * 🔹 What this does
	Batching: Updates counters every 10,000 attempts instead of every loop → less contention, faster.
	
	Per‑stream counters: Each thread has its own counter so you can see contribution.
	
	Global counter: Still tracks total attempts.
	
	Reporter: Prints global progress, ETA, and per‑stream counts every 15 seconds.</br>

 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Sunday 23-August-2026 19:10:19
 */
public class PDFPasswordMultiThread_Batching {
    public static void main(String[] args) throws InterruptedException {
        File pdfFile = new File("D:\\Backup\\Statement\\CC\\SBI Simply Click\\4726426850412133_02092016_PWD_4726426854430487.pdf");
        if (!pdfFile.exists()) {
            System.err.println("Error: PDF file not found!");
            return;
        }

        String prefix = "4726";   // fixed prefix
        String suffix = "0487";   // fixed suffix

        int streams = Runtime.getRuntime().availableProcessors(); // match CPU cores
        long period = 15L;//the period between successive executions
        ExecutorService executor = Executors.newFixedThreadPool(streams);

        long total = 100_000_000L; // 8 unknown digits = 100 million possibilities
        long chunk = total / streams;

        AtomicLong globalCounter = new AtomicLong(0);
//        AtomicLong[] perStreamCounters = new AtomicLong[streams];
        // Arrays to track per-stream counters and previous values
        AtomicLong[] counters = new AtomicLong[streams];
        AtomicLong[] perStreamCounters = new AtomicLong[streams];
        long[] previousCounts = new long[streams];
        for (int i = 0; i < streams; i++) {
        	counters[i] = new AtomicLong(0);
            perStreamCounters[i] = new AtomicLong(0);
            previousCounts[i] = 0;
        }
        
//        for (int i = 0; i < streams; i++) perStreamCounters[i] = new AtomicLong(0);

        Instant startTime = Instant.now();

        // Progress reporter every 15 seconds
        ScheduledExecutorService reporter = Executors.newSingleThreadScheduledExecutor();
        reporter.scheduleAtFixedRate(() -> {
            long checked = globalCounter.get();
            long remaining = total - checked;
            Duration elapsed = Duration.between(startTime, Instant.now());

            double speed = (checked > 0 && elapsed.getSeconds() > 0)
                    ? (double) checked / elapsed.getSeconds()
                    : 0.0;
            long etaSeconds = (speed > 0) ? (long) (remaining / speed) : -1;

            System.out.printf("[%s] Global checked: %,d | Remaining: %,d | Elapsed: %s | ETA: %s%n",
                    LocalDateTime.now(),
                    checked,
                    remaining,
                    formatElapsed(elapsed),
                    (etaSeconds >= 0 ? formatElapsed(Duration.ofSeconds(etaSeconds)) : "N/A"));

            // Per-stream throughput
            for (int i = 0; i < streams; i++) {
            	long current = counters[i].get();
                long prev = perStreamCounters[i].get();
                long diff = current - prev;
                perStreamCounters[i].set(current);

                double streamSpeed = diff / (double) period; // since reporter runs every 15s
                
//                System.out.printf("  Stream %d: %,d%n", i, perStreamCounters[i].get());
                System.out.printf("  Stream %d: %,d total | +%,d in last 15s | ~%.2f rec/s%n",
                        i, current, diff, streamSpeed);
            }
        }, 15, period, TimeUnit.SECONDS);

        for (int s = 0; s < streams; s++) {
            final int streamId = s;
            long start = s * chunk;
            long end = (s == streams - 1) ? total : (s + 1) * chunk;

            executor.submit(() -> {
                int batchCount = 0;
                for (long i = start; i <= end; i++) {
                    String candidate = prefix + String.format("%08d", i) + suffix;

                    // Try password
                    if (tryPassword(pdfFile, candidate)) {
                        System.out.println("Password found: " + candidate);
                        System.exit(0);
                    }

                    batchCount++;
                    if (batchCount == 1000) { // batch update
                        perStreamCounters[streamId].addAndGet(batchCount);
                        globalCounter.addAndGet(batchCount);
                        batchCount = 0;
                    }
                }
                // flush leftover batch
                if (batchCount > 0) {
                    perStreamCounters[streamId].addAndGet(batchCount);
                    globalCounter.addAndGet(batchCount);
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
