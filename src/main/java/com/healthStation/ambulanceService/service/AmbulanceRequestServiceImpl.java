package com.healthStation.ambulanceService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.healthStation.ambulanceService.Exceptions.AmbulanceRequestNotFoundException;
import com.healthStation.ambulanceService.controller.AmbulanceWebSocketController;
import com.healthStation.ambulanceService.model.AmbulanceRequest;
import com.healthStation.ambulanceService.model.AmbulanceRequestStatus;
import com.healthStation.ambulanceService.repository.AmbulanceRepository;
import com.healthStation.ambulanceService.repository.AmbulanceRequestRepository;
import com.healthStation.ambulanceService.utils.Deserializer;
import com.healthStation.ambulanceService.utils.InfoClass;
import com.healthStation.ambulanceService.utils.MyConfig;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AmbulanceRequestServiceImpl implements AmbulanceRequestService{
    private final RestTemplate restTemplate= MyConfig.restTemplate();
    @Autowired
    AmbulanceRequestRepository ambulanceRequestRepository;





    @Override
    public AmbulanceRequest createRequest(AmbulanceRequest request) throws JsonProcessingException {
        ///here the hospital name is hardcoded, since there is only one hospital
        request.setDestHospId(getHospitalId("Hardcoded"));
        request.setStatus(AmbulanceRequestStatus.PENDING);
        request.setRequestedAt(Instant.now());
        String t = restTemplate.getForObject(InfoClass.hospitalLocationEndpoint, String.class);
        Point src = Deserializer.deserialize(t);
        request.setDestinationLocation(src);
        AmbulanceRequest holder=ambulanceRequestRepository.save(request);

        return holder;

    }

    @Override
    public Boolean ambulanceRequestExistsById(Long requestId) {
        return ambulanceRequestRepository.existsById(requestId);
    }

    @Override
    public Optional<AmbulanceRequest> getRequestById(Long requestId) {
        return ambulanceRequestRepository.findById(requestId);

    }

    @Override
    public List<AmbulanceRequest> getAllRequest() {
        return ambulanceRequestRepository.findAll();
    }

    @Override
    public List<AmbulanceRequest> getRequestByPatientId(Long patientId) {
       return ambulanceRequestRepository.findByPatientId(patientId);
    }

    @Override
    public List<AmbulanceRequest> getRequestByStatus(String request) {
        AmbulanceRequestStatus status=null;
        try{
            status=AmbulanceRequestStatus.valueOf(request.toUpperCase());
            return getRequestByStatus(status);
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }

    }
    private List<AmbulanceRequest> getRequestByStatus(AmbulanceRequestStatus requestStatus){
        return ambulanceRequestRepository.findByStatus(requestStatus);
    }

    @Override
    public void deleteRequest(Long requestId) {
        ambulanceRequestRepository.deleteById(requestId);
    }


    @Override
    public AmbulanceRequest updateRequest(Long requestId, AmbulanceRequest updatedRequest) throws AmbulanceRequestNotFoundException {
        // Check if the request exists
        Optional<AmbulanceRequest> optionalExistingRequest = ambulanceRequestRepository.findById(requestId);

        if (optionalExistingRequest.isEmpty()) {
            // If not found, return null or throw an exception (your choice)
            throw new AmbulanceRequestNotFoundException("Ambulance Request doesnt exist!!");
        }

        // Retrieve existing request from DB
        AmbulanceRequest existingRequest = optionalExistingRequest.get();

        // Update only non-null fields
        if (updatedRequest.getStatus() != null) {
            existingRequest.setStatus(updatedRequest.getStatus());
        }
        if (updatedRequest.getPickupLocation() != null) {
            existingRequest.setPickupLocation(updatedRequest.getPickupLocation());
        }

        if (updatedRequest.getRequestedAt() != null) {
            existingRequest.setRequestedAt(MyConfig.minInstant(Instant.now(),updatedRequest.getRequestedAt()));
        }

        if(updatedRequest.getCompletedAt() !=null){
            existingRequest.setCompletedAt(MyConfig.minInstant(Instant.now(),updatedRequest.getCompletedAt()));
        }
       if(updatedRequest.getPickupLocation() !=null){
           existingRequest.setPickupLocation(updatedRequest.getPickupLocation());
       }

        return ambulanceRequestRepository.save(existingRequest);
    }


    @Override
    public AmbulanceRequest updateRequestStatus(Long requestId, String requestStatusString) throws AmbulanceRequestNotFoundException {
        try {
            AmbulanceRequestStatus requestStatus=AmbulanceRequestStatus.valueOf(requestStatusString.toUpperCase());
            Optional<AmbulanceRequest> holder = this.getRequestById(requestId);
            if (holder.isPresent()) {
                AmbulanceRequest ambulanceRequest = holder.get();
                ambulanceRequest.setStatus(requestStatus);
                return ambulanceRequestRepository.save(ambulanceRequest);
            } else {
                throw new AmbulanceRequestNotFoundException("The Request for ambulance by the given Request ID doesnt exist");
            }
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Not a valid Request status");
        }
    }

    @Override
    public AmbulanceRequest updateRequestRequestTime(Long requestId, Instant time) throws AmbulanceRequestNotFoundException {
        Optional<AmbulanceRequest> holder=this.getRequestById(requestId);
        if(holder.isPresent()){
            AmbulanceRequest ambulanceRequest=holder.get();
            ambulanceRequest.setRequestedAt(time);
            return ambulanceRequestRepository.save(ambulanceRequest);
        }else{
            throw new AmbulanceRequestNotFoundException("The Request for ambulance by the given Request ID doesnt exist");
        }
    }

    @Override
    public AmbulanceRequest updateRequestAcceptTime(Long requestId, Instant time) throws AmbulanceRequestNotFoundException {
        Optional<AmbulanceRequest> holder=this.getRequestById(requestId);
        if(holder.isPresent()){
            AmbulanceRequest ambulanceRequest=holder.get();
            ambulanceRequest.setAcceptedAt(time);
            return ambulanceRequestRepository.save(ambulanceRequest);
        }else{
            throw new AmbulanceRequestNotFoundException("The Request for ambulance by the given Request ID doesnt exist");
        }
    }

    @Override
    public AmbulanceRequest updateRequestCompleteTime(Long requestId, Instant time) throws AmbulanceRequestNotFoundException {
        Optional<AmbulanceRequest> holder=this.getRequestById(requestId);
        if(holder.isPresent()){
            AmbulanceRequest ambulanceRequest=holder.get();
            ambulanceRequest.setCompletedAt(time);
            return ambulanceRequestRepository.save(ambulanceRequest);
        }else{
            throw new AmbulanceRequestNotFoundException("The Request for ambulance by the given Request ID doesnt exist");
        }
    }

    @Override
    public AmbulanceRequest updateDestinationHospital(Long requestId, Long destinationHospId) throws AmbulanceRequestNotFoundException {
        Optional<AmbulanceRequest> holder=this.getRequestById(requestId);
        if(holder.isPresent()){
            AmbulanceRequest ambulanceRequest=holder.get();
            ambulanceRequest.setDestHospId(destinationHospId);
            return ambulanceRequestRepository.save(ambulanceRequest);
        }else{
            throw new AmbulanceRequestNotFoundException("The Request for ambulance by the given Request ID doesnt exist");
        }
    }

    @Override
    public void setDestinationLocation(Long requestId) throws AmbulanceRequestNotFoundException, JsonProcessingException {
        Optional<AmbulanceRequest> holder=this.getRequestById(requestId);
        if(holder.isPresent()){
            AmbulanceRequest ambulanceRequest=holder.get();
            //Since in this application we are supporting only one hospital, we won't
            String t = restTemplate.getForObject(InfoClass.hospitalLocationEndpoint, String.class);
            Point src = Deserializer.deserialize(t);
            ambulanceRequest.setDestinationLocation(src);
        }else{
            throw new AmbulanceRequestNotFoundException("The Request for ambulance by the given Request ID doesnt exist");
        }
    }

    @Override
    public Long getHospitalId(String hospitalName) {
        try {

            return restTemplate.getForObject(InfoClass.hospitalIdEndpoint + "/" + hospitalName, Long.class);
        } catch (RestClientException e) {
            System.err.println("Error fetching hospital ID for name: " + hospitalName);
            e.printStackTrace();
            return null;
        }
    }


}
