package com.mondal.notificationservice.service;

import com.mondal.asteroidalerting.event.AsteroidCollisionEvent;
import com.mondal.notificationservice.entity.Notification;
import com.mondal.notificationservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);


    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }


    @KafkaListener(topics = "asteroid-alert", groupId = "notification-service")
    public void alertEvent(AsteroidCollisionEvent notificationEvent) {
        log.info("Received asteroid alerting event: {}", notificationEvent);

        // Create entity for notification
        Notification notification = new Notification(
                notificationEvent.getAsteroidName(),
                LocalDate.parse(notificationEvent.getCloseApproachDate()),
                new BigDecimal(notificationEvent.getMissDistanceKilometers()),
                notificationEvent.getEstimatedDiameterAvgMeters(),
                false
        );


        // Save notification
        final Notification savedNotification = notificationRepository.saveAndFlush(notification);
        log.info("Notification saved: {}", savedNotification);
    }

    @Scheduled(fixedRate = 10000)
    public void sendAlertingEmail() {
        log.info("Triggering scheduled job to send email alerts");
        emailService.sendAsteroidAlertEmail();
    }

}
