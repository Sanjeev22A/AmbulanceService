package com.healthStation.ambulanceService.controller;

import com.healthStation.ambulanceService.model.Ambulance;
import com.healthStation.ambulanceService.service.AmbulanceService;
import com.healthStation.ambulanceService.utils.Deserializer;
import com.healthStation.ambulanceService.utils.InfoClass;
import com.healthStation.ambulanceService.utils.Location;
import com.healthStation.ambulanceService.utils.MyConfig;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("hospitalManagement/apis/test")
public class TestController {

    @Autowired
    AmbulanceService ambulanceService;

    @GetMapping("/all")
    public ResponseEntity<List<Ambulance>> getAllAmbulance(){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ambulanceService.getAllAmbulance());
    }

    @GetMapping("/nearby/{count}")
    public List<Ambulance> getNearbyAmbulance(@PathVariable Long count){
        return null;
        //Ahh i forgot to think about the source location , let me get back to it later
        //return ambulanceService.findNearbyAmbulance(count);
    }

    @GetMapping("/hospital/hospitalLocation")
    public ResponseEntity<Location> getHospitalLocation(){

        GeometryFactory geometryFactory=new GeometryFactory(new PrecisionModel(),4326);

        Coordinate hospitalCoordinate=new Coordinate(80.207,13.0827);

        Point hospitalPoint=geometryFactory.createPoint(hospitalCoordinate);
        Location hospitalLoc=new Location(hospitalPoint);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hospitalLoc);


    }
    @GetMapping("/test/test1")
    public ResponseEntity<String> test1(){
        String t=MyConfig.restTemplate().getForObject(InfoClass.hospitalLocationEndpoint, String.class);
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }
    @GetMapping("/test/endpoint")
    public ResponseEntity<Location> test(){
        try {
            String t = MyConfig.restTemplate().getForObject(InfoClass.hospitalLocationEndpoint, String.class);
            Point p = Deserializer.deserialize(t);
            Location loc=new Location(p);
            return ResponseEntity
                    .ok(loc);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
