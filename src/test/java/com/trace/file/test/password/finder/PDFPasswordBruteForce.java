package com.trace.file.test.password.finder;

import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.File;
import java.io.IOException;
/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Sunday 23-August-2026 16:16:34
 */
public class PDFPasswordBruteForce {
    public static void main(String[] args) {
        File pdfFile = new File("protected.pdf"); // change to your file path

        // Fixed prefix and suffix
        String prefix = "4";
        String suffix = "0487";

        // Loop through all 11-digit combinations
        long start = 0L;
        long end = 99_999_999_999L; // 11 digits max

        for (long i = start; i <= end; i++) {
            String middle = String.format("%011d", i); // pad with leading zeros
            String candidate = prefix + middle + suffix;

            if (tryPassword(pdfFile, candidate)) {
                System.out.println("Password found: " + candidate);
                break;
            }

            // Optional: progress indicator
            if (i % 1_000_000 == 0) {
                System.out.println("Checked up to: " + candidate);
            }
        }
    }

    private static boolean tryPassword(File pdfFile, String password) {
        try (PDDocument doc = PDDocument.load(pdfFile, password)) {
            // If load succeeds, password is correct
            return true;
        } catch (IOException e) {
            // Wrong password or other error
            return false;
        }
    }
}
