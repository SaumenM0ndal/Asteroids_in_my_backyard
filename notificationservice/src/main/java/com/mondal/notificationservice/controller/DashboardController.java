package com.mondal.notificationservice.controller;

import com.mondal.notificationservice.entity.Notification;
import com.mondal.notificationservice.entity.User;
import com.mondal.notificationservice.repository.NotificationRepository;
import com.mondal.notificationservice.repository.UserRepository;
import com.mondal.notificationservice.service.AsteroidAlertingClient;
import com.mondal.notificationservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/ui")
public class DashboardController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AsteroidAlertingClient asteroidAlertingClient;
    private final EmailService emailService;

    @Autowired
    public DashboardController(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               AsteroidAlertingClient asteroidAlertingClient,
                               EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.asteroidAlertingClient = asteroidAlertingClient;
        this.emailService = emailService;
    }

    @GetMapping("/stats")
    public DashboardStatsResponse getStats() {
        return new DashboardStatsResponse(
                notificationRepository.count(),
                notificationRepository.countByEmailSent(false),
                userRepository.count(),
                userRepository.findAllEmailsAndNotificationEnabled().size()
        );
    }

    @GetMapping("/notifications")
    public List<NotificationResponse> getNotifications() {
        return notificationRepository.findTop100ByOrderByIdDesc().stream()
                .map(this::toNotificationResponse)
                .toList();
    }

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return userRepository.findAllByOrderByIdDesc().stream()
                .map(this::toUserResponse)
                .toList();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        final String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);
        if (!normalizedEmail.contains("@") || normalizedEmail.startsWith("@") || normalizedEmail.endsWith("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email format is invalid");
        }

        if (userRepository.findByEmailIgnoreCase(normalizedEmail).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        final String fullName = request.fullName() == null || request.fullName().isBlank()
                ? normalizedEmail
                : request.fullName().trim();

        final User savedUser = userRepository.save(new User(
                fullName,
                normalizedEmail,
                request.notificationEnabled() == null || request.notificationEnabled()
        ));
        return toUserResponse(savedUser);
    }

    @PatchMapping("/users/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Update payload is required");
        }

        final User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (request.fullName() != null) {
            user.setFullName(request.fullName().trim());
        }

        if (request.notificationEnabled() != null) {
            user.setNotificationEnabled(request.notificationEnabled());
        }

        final User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    @PostMapping("/trigger-alert")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void triggerAlert() {
        try {
            asteroidAlertingClient.triggerAlertScan();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Unable to reach asteroid alerting service", ex);
        }
    }

    @PostMapping("/send-emails")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendEmailsNow() {
        emailService.sendAsteroidAlertEmail();
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getAsteroidName(),
                notification.getCloseApproachDate(),
                notification.getMissDistanceKilometers() == null ? null : notification.getMissDistanceKilometers().toPlainString(),
                notification.getEstimatedDiameterAvgMeters(),
                notification.isEmailSent()
        );
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.isNotificationEnabled()
        );
    }

    public record DashboardStatsResponse(long totalNotifications,
                                         long pendingEmails,
                                         long totalUsers,
                                         long subscribedUsers) {
    }

    public record NotificationResponse(Long id,
                                       String asteroidName,
                                       LocalDate closeApproachDate,
                                       String missDistanceKilometers,
                                       double estimatedDiameterAvgMeters,
                                       boolean emailSent) {
    }

    public record UserResponse(Long id,
                               String fullName,
                               String email,
                               boolean notificationEnabled) {
    }

    public record CreateUserRequest(String fullName,
                                    String email,
                                    Boolean notificationEnabled) {
    }

    public record UpdateUserRequest(String fullName,
                                    Boolean notificationEnabled) {
    }
}


