package com.mondal.notificationservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AsteroidAlertingClient {

    private final RestClient restClient;

    public AsteroidAlertingClient(@Value("${asteroidalerting.base-url:http://localhost:8080}") String asteroidAlertingBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(asteroidAlertingBaseUrl)
                .build();
    }

    public void triggerAlertScan() {
        restClient.post()
                .uri("/api/v1/asteroid-alerting/alert")
                .retrieve()
                .toBodilessEntity();
    }
}

