package com.trace.file.service.impl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.trace.file.entity.FileUpload;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Thursday 03-September-2026 15:25:07
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendNotifyEmail(String to, String fileName, String assignedBy) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("File Assignment Notification");
        message.setText("You have been assigned file: " + fileName +
                        " by " + assignedBy + ". Please start the password finder.");
        mailSender.send(message);
    }
    
    /**
     * Sends an HTML email notification with file assignment details.</br>
     * 
     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Thursday 03-September-2026 16:12:31
     * @param to
     * @param file
     * @throws MessagingException
     */
    public void sendNotifyEmail(String to, FileUpload file) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("File Assignment Notification - " + file.getFileName());

        String htmlBody = """
            <html>
              <body style="font-family: Arial, sans-serif;">
                <h2 style="color:#1976d2;">File Assignment Notification</h2>
                <p>You have been assigned a new file. Details are below:</p>
                <table style="border-collapse: collapse; width: 100%;">
                  <tr><td><b>File ID:</b></td><td>%d</td></tr>
                  <tr><td><b>File Name:</b></td><td>%s</td></tr>
                  <tr><td><b>Assigned By:</b></td><td>%s</td></tr>
                  <tr><td><b>Assigned To:</b></td><td>%s</td></tr>
                  <tr><td><b>Assign Date:</b></td><td>%s</td></tr>
                  <tr><td><b>Password Hint:</b></td><td>%s</td></tr>
                  <tr><td><b>Contact Email:</b></td><td>%s</td></tr>
                </table>
                <p style="margin-top:20px;">Please start the password finder process promptly.</p>
              </body>
            </html>
            """.formatted(
                file.getId(),
                file.getFileName(),
                file.getAssignedBy(),
                file.getAssignedTo(),
                file.getAssignDate(),
                file.getPasswordHint(),
                file.getContactEmail()
            );

        helper.setText(htmlBody, true); // true = HTML
        mailSender.send(message);
    }
}
