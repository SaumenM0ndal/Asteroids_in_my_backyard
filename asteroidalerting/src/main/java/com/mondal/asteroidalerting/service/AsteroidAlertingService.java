package com.mondal.asteroidalerting.service;

import com.mondal.asteroidalerting.client.NasaClient;
import com.mondal.asteroidalerting.dto.Asteroid;
import com.mondal.asteroidalerting.event.AsteroidCollisionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AsteroidAlertingService {

    private final NasaClient nasaClient;
    private final KafkaTemplate<String, AsteroidCollisionEvent> kafkaTemplate;
    private final Logger log = LoggerFactory.getLogger(AsteroidAlertingService.class);

    @Autowired
    public AsteroidAlertingService(NasaClient nasaClient, KafkaTemplate<String, AsteroidCollisionEvent> kafkaTemplate) {
        this.nasaClient = nasaClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void alert() {
        log.info("Alerting service called");

        // From and to date
        final LocalDate fromDate = LocalDate.now();
        final LocalDate toDate = LocalDate.now().plusDays(7);

        // Call NASA API to get the asteroid data
        log.info("Getting asteroid list for dates: {} to {}", fromDate, toDate);
        final List<Asteroid> asteroidList =  nasaClient.getNeoAsteroids(fromDate, toDate);
        log.info("Retrieved Asteroid list of size: {}", asteroidList.size());

        // If there are any hazardous asteroids, send an alert
        final List<Asteroid> dangerousAsteroids = asteroidList.stream()
                .filter(Asteroid::isPotentiallyHazardous)
                .toList();
        log.info("Found {} hazardous asteroids", dangerousAsteroids.size());

        // Create an alert and put on Kafka topic
        final List<AsteroidCollisionEvent> asteroidCollisionEventList =
                createEventLisOfDangerousAsteroids(dangerousAsteroids);

        log.info("Sending {} asteroid alerts to Kafka", asteroidCollisionEventList.size());
        asteroidCollisionEventList.forEach(event -> {
            kafkaTemplate.send("asteroid-alert", event);
            log.info("Asteroid alert sent to Kafka topic: {}", event);
        });
    }

    private List<AsteroidCollisionEvent> createEventLisOfDangerousAsteroids(final List<Asteroid> dangerousAsteroids) {
        return dangerousAsteroids.stream()
                .map(asteroid -> new AsteroidCollisionEvent(
                        asteroid.getName(),
                        asteroid.getCloseApproachData().get(0).getCloseApproachDate().toString(),
                        asteroid.getCloseApproachData().get(0).getMissDistance().getKilometers(),
                        (asteroid.getEstimatedDiameter().getMeters().getMinDiameter() +
                                asteroid.getEstimatedDiameter().getMeters().getMaxDiameter()) / 2
                ))
                .toList();
    }
}
