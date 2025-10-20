package com.healthStation.ambulanceService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.healthStation.ambulanceService.Exceptions.AmbulanceRequestNotFoundException;
import com.healthStation.ambulanceService.model.AmbulanceRequest;
import com.healthStation.ambulanceService.model.AmbulanceRequestStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AmbulanceRequestService {
    AmbulanceRequest createRequest(AmbulanceRequest request) throws JsonProcessingException;
    Boolean ambulanceRequestExistsById(Long requestId);
    Optional<AmbulanceRequest> getRequestById(Long requestId);
    List<AmbulanceRequest> getAllRequest();

    List<AmbulanceRequest> getRequestByPatientId(Long patientId);
    List<AmbulanceRequest> getRequestByStatus(String request);

    void deleteRequest(Long requestId);
    AmbulanceRequest updateRequest(Long requestId,AmbulanceRequest request) throws AmbulanceRequestNotFoundException;



    AmbulanceRequest updateRequestStatus(Long requestId, String requestStatusString) throws AmbulanceRequestNotFoundException;

    AmbulanceRequest updateRequestRequestTime(Long requestId, Instant time) throws AmbulanceRequestNotFoundException;
    AmbulanceRequest updateRequestAcceptTime(Long requestId,Instant time) throws AmbulanceRequestNotFoundException;
    AmbulanceRequest updateRequestCompleteTime(Long requestId,Instant time) throws AmbulanceRequestNotFoundException;

    AmbulanceRequest updateDestinationHospital(Long requestId,Long destinationHospId) throws AmbulanceRequestNotFoundException;

    void setDestinationLocation(Long requestId) throws AmbulanceRequestNotFoundException, JsonProcessingException;
    Long getHospitalId(String hospitalName);
}
