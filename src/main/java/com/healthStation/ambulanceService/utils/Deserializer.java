package com.healthStation.ambulanceService.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.List;
import java.util.Map;

public class Deserializer {
    public static Point deserialize(String str) throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        Map<String,Object> map=mapper.readValue(str, new TypeReference<Map<String, Object>>() {});
        Map<String,Object> hospitalCoords=(Map<String,Object>) map.get("hospitalCoordinate");
        List<Double> coords=(List<Double>)hospitalCoords.get("coordinates");

        GeometryFactory geometryFactory=new GeometryFactory(new PrecisionModel(),4326);

        Coordinate hospitalCoordinate=new Coordinate(coords.get(0),coords.get(1));

        return geometryFactory.createPoint(hospitalCoordinate);
    }
}
