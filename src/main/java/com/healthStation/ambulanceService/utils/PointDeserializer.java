package com.healthStation.ambulanceService.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class PointDeserializer extends JsonDeserializer<Point> {
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Override
    public Point deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode coordinates = node.get("coordinates");
        double lon = coordinates.get(0).asDouble();
        double lat = coordinates.get(1).asDouble();
        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }
}
