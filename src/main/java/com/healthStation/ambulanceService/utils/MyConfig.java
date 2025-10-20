package com.healthStation.ambulanceService.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Configuration
public class MyConfig {

    @Bean
    public static RestTemplate restTemplate(){
        return new RestTemplate();
    }
    public static Instant minInstant(Instant a, Instant b) {
        return a.isBefore(b) ? a : b;
    }

}
