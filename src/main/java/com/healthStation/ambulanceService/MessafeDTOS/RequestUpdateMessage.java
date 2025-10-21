package com.healthStation.ambulanceService.MessafeDTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateMessage {
    private String event; //Assigned or Cancelled
    private Long requestId;
    private Long assignedAmbulanceId;

}
