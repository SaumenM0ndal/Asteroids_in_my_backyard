package com.mondal.asteroidalerting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class NasaNeoResponse {

    @JsonProperty("near_earth_objects")
    private Map<String, List<Asteroid>> nearEarthObjects;

    @JsonProperty("element_count")
    private Long totalAsteroids;

    public Map<String, List<Asteroid>> getNearEarthObjects() {
        return nearEarthObjects;
    }

    public void setNearEarthObjects(Map<String, List<Asteroid>> nearEarthObjects) {
        this.nearEarthObjects = nearEarthObjects;
    }

    public Long getTotalAsteroids() {
        return totalAsteroids;
    }

    public void setTotalAsteroids(Long totalAsteroids) {
        this.totalAsteroids = totalAsteroids;
    }
}
