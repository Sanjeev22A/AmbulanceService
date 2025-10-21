package com.healthStation.ambulanceService.MessageDTOS;

import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
public class AmbulanceNotificationMessage {
    private Long requestId;
    private Point location;
    private String serviceType;
}
