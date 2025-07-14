package com.mondal.asteroidalerting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CloseApproachData {

    @JsonProperty("close_approach_date")
    private LocalDate closeApproachDate;

    public LocalDate getCloseApproachDate() {
        return closeApproachDate;
    }

    public void setCloseApproachDate(LocalDate closeApproachDate) {
        this.closeApproachDate = closeApproachDate;
    }

    public MissDistance getMissDistance() {
        return missDistance;
    }

    public void setMissDistance(MissDistance missDistance) {
        this.missDistance = missDistance;
    }

    @JsonProperty("miss_distance")
    private MissDistance missDistance;
}
