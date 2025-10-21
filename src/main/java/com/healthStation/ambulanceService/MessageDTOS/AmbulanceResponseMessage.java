package com.healthStation.ambulanceService.MessageDTOS;

import lombok.Data;

@Data
public class AmbulanceResponseMessage {
    private Long requestId;
    private Long ambulanceId;
    private String response; //Accept or reject
}

