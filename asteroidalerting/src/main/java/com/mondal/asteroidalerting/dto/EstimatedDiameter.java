package com.mondal.asteroidalerting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EstimatedDiameter {

    @JsonProperty("meters")
    private DiameterRange meters;

    public DiameterRange getMeters() {
        return meters;
    }

    public void setMeters(DiameterRange meters) {
        this.meters = meters;
    }
}
