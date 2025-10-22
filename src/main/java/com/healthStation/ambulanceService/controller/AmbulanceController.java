package com.healthStation.ambulanceService.controller;
//Fake push
import com.healthStation.ambulanceService.Exceptions.AmbulanceNotFoundException;
import com.healthStation.ambulanceService.Exceptions.AmbulanceRequestNotFoundException;
import com.healthStation.ambulanceService.model.Ambulance;
import com.healthStation.ambulanceService.model.AmbulanceRequest;
import com.healthStation.ambulanceService.model.AmbulanceRequestStatus;
import com.healthStation.ambulanceService.model.PointDTO;
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
    ///Getting hospital ID from hospital name
    @GetMapping("/hospital/Id/{name}")
    public ResponseEntity<Long> getHospitalId(@PathVariable String name){
        //For our application we have just one hospital so we have hardcoded its id.
        return ResponseEntity.status(HttpStatus.OK).body(33L);
    }
    ///Getting hospital Location, again for our application since there is only one hospital it has been hardcoded.
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

    @GetMapping("/ambulance/getByDriverId/{id}")
    public ResponseEntity<List<Ambulance>> getAmbulanceByDriverId(@PathVariable Long id){
        try{
            List<Ambulance> ambulanceList=ambulanceService.findByDriverId(id);
            return ResponseEntity.status(HttpStatus.OK).body(ambulanceList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    @PatchMapping("/ambulance/poll/location/{id}")
    public ResponseEntity<?> pollAmbulanceLocation(@PathVariable Long id,@RequestBody PointDTO location){
        try{
            GeometryFactory geometryFactory=new GeometryFactory(new PrecisionModel(),4326);
            Point loc=geometryFactory.createPoint(
                    new Coordinate(location.getCoordinates()[0],location.getCoordinates()[1])
            );
            Ambulance holder=ambulanceService.pollAmbulanceLocation(id,loc);
            return ResponseEntity.status(HttpStatus.OK).body(holder);
        }catch(AmbulanceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested Ambulance by ambulanceID wasnt found");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    ///End of Ambulance CRUD Operations
    ///-------------------------------------------------
    ///Ambulance Request Controller from here

    @PostMapping("/ambulanceRequest/Request")
    public ResponseEntity<AmbulanceRequest> requestAmbulance(@RequestBody AmbulanceRequest ambulanceRequest){
        try {
            AmbulanceRequest ambulanceRequest1=ambulanceRequestService.createRequest(ambulanceRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ambulanceRequest1);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/ambulanceRequest/get/{requestId}")
    public ResponseEntity<AmbulanceRequest> getAmbulanceRequestById(@PathVariable Long requestId){
        try{
            Optional<AmbulanceRequest> ambulanceRequest=ambulanceRequestService.getRequestById(requestId);
            return ambulanceRequest.map(request -> ResponseEntity.status(HttpStatus.OK).body(request)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ambulanceRequest/getAll")
    public ResponseEntity<List<AmbulanceRequest>> getAllAmbulanceRequest(){
        try{
            List<AmbulanceRequest> ambulanceRequests=ambulanceRequestService.getAllRequest();
            return ResponseEntity.status(HttpStatus.OK).body(ambulanceRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/ambulanceRequest/getByPaitentId/{id}")
    public ResponseEntity<List<AmbulanceRequest>> getAmbulanceRequestByPaitentId(@PathVariable Long id){
        try{
            List<AmbulanceRequest> ambulanceRequests=ambulanceRequestService.getRequestByPatientId(id);
            return ResponseEntity.status(HttpStatus.OK).body(ambulanceRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/ambulanceRequest/getByStatus/{ambulanceRequestStatus}")
    public ResponseEntity<?> getAmbulanceRequestByStatus(@PathVariable String ambulanceRequestStatus){
        try{
            List<AmbulanceRequest> ambulanceRequests=ambulanceRequestService.getRequestByStatus(ambulanceRequestStatus);
            return ResponseEntity.status(HttpStatus.OK).body(ambulanceRequests);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/ambulanceRequest/deleteRequest/{requestId}")
    public ResponseEntity<String> deleteAmbulanceRequest(@PathVariable Long requestId){
        try{
            if(ambulanceRequestService.ambulanceRequestExistsById(requestId)) {
                ambulanceRequestService.deleteRequest(requestId);
                return ResponseEntity.status(HttpStatus.OK).body("Ambulance request has been deleted");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ambulance request doesnt exist");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/ambulanceRequest/updateRequest/{requestId}")
    public ResponseEntity<AmbulanceRequest> updateAmbulanceRequest(@PathVariable Long requestId,@RequestBody AmbulanceRequest ambulanceRequest){
        try{
            AmbulanceRequest request=ambulanceRequestService.updateRequest(requestId,ambulanceRequest);
            return ResponseEntity.status(HttpStatus.OK).body(request);
        } catch (AmbulanceRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PatchMapping("/ambulanceRequest/updateRequestStatus/{requestId}")
    public ResponseEntity<String> updateAmbulanceRequestStatus(@PathVariable Long requestId,@RequestBody String requestStatus){
        try{
            AmbulanceRequest request=ambulanceRequestService.updateRequestStatus(requestId,requestStatus);
            return ResponseEntity.status(HttpStatus.OK).body("Request Status has been updated");
        }catch (AmbulanceRequestNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ambulance Request not found");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not a valid Request status");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    ///End of Ambulance Request CRUD Operations
    ///-------------------------------------------------
    ///Ambulance Assignment Request from here


}
