package com.healthStation.ambulanceService.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Getter
public class Location {
    @JsonSerialize(using=PointSerializer.class)
    private Point hospitalCoordinate;
    public Location(Point hospitalCoordinate){
        this.hospitalCoordinate=hospitalCoordinate;
    }

}
