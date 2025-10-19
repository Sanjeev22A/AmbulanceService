package com.healthStation.ambulanceService.controller;

import com.healthStation.ambulanceService.model.Ambulance;
import com.healthStation.ambulanceService.service.AmbulanceAssignmentService;
import com.healthStation.ambulanceService.service.AmbulanceNotificationService;
import com.healthStation.ambulanceService.service.AmbulanceRequestService;
import com.healthStation.ambulanceService.service.AmbulanceService;
import com.healthStation.ambulanceService.utils.Location;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hospitalManagement/apis/ambulanceService")
public class AmbulanceController {
    @Autowired
    AmbulanceService ambulanceService;

    @Autowired
    AmbulanceAssignmentService ambulanceAssignmentService;

    @Autowired
    AmbulanceNotificationService ambulanceNotificationService;

    @Autowired
    AmbulanceRequestService ambulanceRequestService;

    ///Ambulance CRUDS here
    @PostMapping("/ambulance/register")
    public ResponseEntity<Ambulance> registerAmbulance(@RequestBody Ambulance ambulance){
        try{
            Ambulance holder=ambulanceService.registerAmbulance(ambulance);

            return ResponseEntity.status(HttpStatus.OK).body(holder);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ambulance/get/all")
    public ResponseEntity<List<Ambulance>> getAllAmbulance(){
        try {
            List<Ambulance> ambulanceList = ambulanceService.getAllAmbulance();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ambulanceList);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GetMapping("/ambulance/get/{id}")
    public ResponseEntity<Ambulance> getAmbulanceById(@PathVariable Long id){
        try {
            Optional<Ambulance> ambulance = ambulanceService.getAmbulanceById(id);
            return ambulance.map(value -> ResponseEntity.status(HttpStatus.OK).body(value)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @DeleteMapping("/ambulance/{id}")
    public ResponseEntity<String> deleteAmbulance(@PathVariable Long id){
        Boolean flag=ambulanceService.deleteAmbulance(id);
        if(flag){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("The ambulance with id:"+id+" has been deleted");
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("The ambulance with id:"+id+" either couldn't be found or wasn't deleted");
        }
    }

    @PatchMapping("/ambulance/update/{id}")
    public ResponseEntity<Ambulance> updateAmbulance(@PathVariable Long id,@RequestBody Ambulance ambulance){
        try{
            Ambulance holder=ambulanceService.updateAmbulance(id,ambulance);
            return ResponseEntity.status(HttpStatus.OK).body(holder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    ///Ambulance Assignment Request from here


}
