package com.healthStation.ambulanceService.MessafeDTOS;

import com.healthStation.ambulanceService.model.AmbulanceServiceType;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
public class AmbulanceNotificationMessage {
    private Long requestId;
    private Point location;
    private String serviceType;
}
