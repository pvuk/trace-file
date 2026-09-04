package com.trace.file.test.password.finder;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.File;
import java.io.IOException;
/**
 * Here’s how you can extend the controller/worker model so the controller not only launches multiple JVM workers but also monitors their output. As soon as one worker prints "Password found", the controller will terminate all other processes to avoid wasting cycles.</br>
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Sunday 23-August-2026 16:25:07
 */
public class Worker {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java Worker <pdfPath> <start> <end>");
            return;
        }

        File pdfFile = new File(args[0]);
        if (!pdfFile.exists()) {
            System.err.println("Error: PDF file not found!");
            return;
        }

        long start = Long.parseLong(args[1]);
        long end = Long.parseLong(args[2]);

        String prefix = "4";
        String suffix = "0487";

        for (long i = start; i <= end; i++) {
            String middle = String.format("%011d", i);
            String candidate = prefix + middle + suffix;

            if (tryPassword(pdfFile, candidate)) {
                System.out.println("Password found: " + candidate);
                System.exit(0); // stop this worker immediately
            }
        }
    }

    private static boolean tryPassword(File pdfFile, String password) {
        try (PDDocument doc = PDDocument.load(pdfFile, password)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
