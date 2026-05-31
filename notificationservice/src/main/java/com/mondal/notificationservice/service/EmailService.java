package com.mondal.notificationservice.service;

import com.mondal.notificationservice.entity.Notification;
import com.mondal.notificationservice.repository.NotificationRepository;
import com.mondal.notificationservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${email.service.from.email}")
    private String fromEmail;

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(NotificationRepository notificationRepository,
                        UserRepository userRepository,
                        JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Async
    public void sendAsteroidAlertEmail() {
        final List<String> toEmails = userRepository.findAllEmailsAndNotificationEnabled();
        if (toEmails.isEmpty()) {
            log.info("No users to send email to");
            return;
        }

        final List<Notification> pendingNotifications = notificationRepository.findByEmailSent(false);
        if (pendingNotifications.isEmpty()) {
            log.info("No asteroids to send alerts for at {}", LocalDateTime.now());
            return;
        }

        final String text = createEmailText(pendingNotifications);

        toEmails.forEach(toEmail -> sendEmail(toEmail, text));

        pendingNotifications.forEach(notification -> notification.setEmailSent(true));
        notificationRepository.saveAll(pendingNotifications);
        log.info("Email sent to: #{} users", toEmails.size());
    }

    private void sendEmail(final String toEmail, final String text) {
        // send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(fromEmail);
        message.setSubject("Nasa Asteroid Collision Event");
        message.setText(text);
        mailSender.send(message);
    }

    private String createEmailText(final List<Notification> notificationList) {
        StringBuilder emailText = new StringBuilder();
        emailText.append("Asteroid Alert: \n");
        emailText.append("=====================================\n");

        notificationList.forEach(notification -> {
            emailText.append("Asteroid Name: ").append(notification.getAsteroidName()).append("\n");
            emailText.append("Close Approach Date: ").append(notification.getCloseApproachDate()).append("\n");
            emailText.append("Estimated Diameter Avg Meters: ").append(notification.getEstimatedDiameterAvgMeters()).append("\n");
            emailText.append("Miss Distance Kilometers: ").append(notification.getMissDistanceKilometers()).append("\n");
            emailText.append("=====================================\n");
        });

        return emailText.toString();
    }
}
