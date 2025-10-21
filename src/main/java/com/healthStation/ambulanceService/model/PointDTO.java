package com.healthStation.ambulanceService.model;

import lombok.Data;

@Data
public class PointDTO {
    private String type;
    private Double[] coordinates;
}
