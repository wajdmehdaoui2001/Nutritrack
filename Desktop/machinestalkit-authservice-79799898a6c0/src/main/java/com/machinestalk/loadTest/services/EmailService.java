package com.machinestalk.loadTest.services;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        sendEmail(toEmail, "Password Reset Request", "<p>Click the link to reset your password: </p>" +
                "<a href='" + resetLink + "'>" + resetLink + "</a>");
    }
    public void sendWelcomeEmail(String toEmail, String resetLink) {
        sendEmail(toEmail, "Welcome on Board!", "<p>Welcome to our platform! Here is your link to set your password:</p>" +
                "<a href='" + resetLink + "'>" + resetLink + "</a>");

    }

    private void sendEmail(String toEmail, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("wajdmahdaoui@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            System.out.println(subject + " email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

}

