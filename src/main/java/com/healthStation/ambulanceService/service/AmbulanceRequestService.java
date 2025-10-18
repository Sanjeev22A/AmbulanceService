package com.healthStation.ambulanceService.service;

import com.healthStation.ambulanceService.model.AmbulanceRequest;

import java.util.List;
import java.util.Optional;

public interface AmbulanceRequestService {
    AmbulanceRequest createRequest(AmbulanceRequest request);

    Optional<AmbulanceRequest> getRequestById(Long requestId);
    List<AmbulanceRequest> getAllRequest();

    List<AmbulanceRequest> getRequestByPatientId(Long patientId);
    List<AmbulanceRequest> getRequestByStatus(String request);

    void deleteRequest(Long requestId);
    AmbulanceRequest updateRequest(AmbulanceRequest request);

}
