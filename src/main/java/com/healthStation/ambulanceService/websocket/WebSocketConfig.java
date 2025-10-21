package com.healthStation.ambulanceService.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws")  //The endpoint to which the client must connect to , to use websocket
                .setAllowedOrigins("*") // For cross origin domains, set to the application path when deploying
                .withSockJS(); // The fallback, if websockets isnt supported
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/app"); //When ever client side messages starts with /app, deliver it to the handler
        registry.enableSimpleBroker("/topic"); //For broadcast from server side
    }
}
