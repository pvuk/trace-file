package com.trace.file.test.password.finder;
import java.io.*;
import java.util.*;
/**
 * ⚡ How It Works
Controller splits the range into chunks and spawns multiple JVM workers.

Each Worker brute‑forces its assigned range.

The Controller monitors worker output in real time.

If any worker prints "Password found", the controller immediately kills all other workers and exits.</br>

🚨 Notes
Adjust workers based on how many JVM processes you want (and how many cores/machines you have).

This design can be extended to run workers on different machines by copying the PDF and running the worker program with assigned ranges.

Even with parallelization, brute‑forcing 100 billion possibilities is still computationally extreme — but this pattern ensures you don’t waste cycles once success is achieved.</br>



 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Sunday 23-August-2026 16:27:24
 */
public class Controller {
    public static void main(String[] args) throws IOException {
        File pdfFile = new File("C:\\Users\\Priyanka\\Downloads\\4726426850412133_02092016.pdf");
        if (!pdfFile.exists()) {
            System.err.println("Error: PDF file not found!");
            return;
        }

        long total = 99_999_999_999L; // 11 digits
        int workers = 4;              // number of JVM processes
        long chunk = total / workers;

        List<Process> processes = new ArrayList<>();
        List<Thread> monitors = new ArrayList<>();

        for (int w = 0; w < workers; w++) {
            long start = w * chunk;
            long end = (w == workers - 1) ? total : (w + 1) * chunk;

            ProcessBuilder pb = new ProcessBuilder(
                "java", "-cp", "target/classes:lib/*", "Worker",
                pdfFile.getAbsolutePath(),
                String.valueOf(start),
                String.valueOf(end)
            );

            pb.redirectErrorStream(true);
            Process p = pb.start();
            processes.add(p);

            // Monitor each worker's output
            Thread monitor = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(p.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[Worker " + start + "-" + end + "] " + line);
                        if (line.contains("Password found")) {
                            // Kill all other processes
                            for (Process other : processes) {
                                if (other != p) {
                                    other.destroy();
                                }
                            }
                            System.exit(0); // stop controller once password is found
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            monitor.start();
            monitors.add(monitor);
        }

        // Wait for all monitors to finish
        for (Thread t : monitors) {
            try {
                t.join();
            } catch (InterruptedException ignored) {}
        }
    }
}
