package com.mondal.asteroidalerting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiameterRange {

    @JsonProperty("estimated_diameter_min")
    private double minDiameter;

    @JsonProperty("estimated_diameter_max")
    private double maxDiameter;

    public double getMinDiameter() {
        return minDiameter;
    }

    public void setMinDiameter(double minDiameter) {
        this.minDiameter = minDiameter;
    }

    public double getMaxDiameter() {
        return maxDiameter;
    }

    public void setMaxDiameter(double maxDiameter) {
        this.maxDiameter = maxDiameter;
    }
}
