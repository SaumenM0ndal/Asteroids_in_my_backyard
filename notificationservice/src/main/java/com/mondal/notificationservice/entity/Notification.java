package com.mondal.notificationservice.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String asteroidName;
    private LocalDate closeApproachDate;
    private BigDecimal missDistanceKilometers;
    private double estimatedDiameterAvgMeters;
    private boolean emailSent;

    public Notification(String asteroidName, LocalDate closeApproachDate, BigDecimal missDistanceKilometers, double estimatedDiameterAvgMeters, boolean emailSent) {
        this.asteroidName = asteroidName;
        this.closeApproachDate = closeApproachDate;
        this.missDistanceKilometers = missDistanceKilometers;
        this.estimatedDiameterAvgMeters = estimatedDiameterAvgMeters;
        this.emailSent = emailSent;
    }

    //Add this no-args constructor (required by JPA)
    public Notification() {
    }

    public String getAsteroidName() {
        return asteroidName;
    }

    public void setAsteroidName(String asteroidName) {
        this.asteroidName = asteroidName;
    }

    public LocalDate getCloseApproachDate() {
        return closeApproachDate;
    }

    public void setCloseApproachDate(LocalDate closeApproachDate) {
        this.closeApproachDate = closeApproachDate;
    }

    public BigDecimal getMissDistanceKilometers() {
        return missDistanceKilometers;
    }

    public void setMissDistanceKilometers(BigDecimal missDistanceKilometers) {
        this.missDistanceKilometers = missDistanceKilometers;
    }

    public double getEstimatedDiameterAvgMeters() {
        return estimatedDiameterAvgMeters;
    }

    public void setEstimatedDiameterAvgMeters(double estimatedDiameterAvgMeters) {
        this.estimatedDiameterAvgMeters = estimatedDiameterAvgMeters;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }
}
