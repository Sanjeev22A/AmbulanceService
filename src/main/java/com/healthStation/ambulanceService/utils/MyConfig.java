package com.healthStation.ambulanceService.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyConfig {

    @Bean
    public static RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
