package com.mondal.asteroidalerting.event;

public class AsteroidCollisionEvent {

    private String asteroidName;
    private String closeApproachDate;
    private String missDistanceKilometers;
    private double estimatedDiameterAvgMeters;

    public AsteroidCollisionEvent(String asteroidName, String closeApproachDate, String missDistanceKilometers, double estimatedDiameterAvgMeters) {
        this.asteroidName = asteroidName;
        this.closeApproachDate = closeApproachDate;
        this.missDistanceKilometers = missDistanceKilometers;
        this.estimatedDiameterAvgMeters = estimatedDiameterAvgMeters;
    }

    public AsteroidCollisionEvent() {
        // no-args constructor required for Jackson
    }

    public String getAsteroidName() {
        return asteroidName;
    }

    public void setAsteroidName(String asteroidName) {
        this.asteroidName = asteroidName;
    }

    public String getCloseApproachDate() {
        return closeApproachDate;
    }

    public void setCloseApproachDate(String closeApproachDate) {
        this.closeApproachDate = closeApproachDate;
    }

    public String getMissDistanceKilometers() {
        return missDistanceKilometers;
    }

    public void setMissDistanceKilometers(String missDistanceKilometers) {
        this.missDistanceKilometers = missDistanceKilometers;
    }

    public double getEstimatedDiameterAvgMeters() {
        return estimatedDiameterAvgMeters;
    }

    public void setEstimatedDiameterAvgMeters(double estimatedDiameterAvgMeters) {
        this.estimatedDiameterAvgMeters = estimatedDiameterAvgMeters;
    }
}
