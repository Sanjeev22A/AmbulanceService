package com.healthStation.ambulanceService.service;

import com.healthStation.ambulanceService.model.AmbulanceRequest;
import com.healthStation.ambulanceService.model.AmbulanceRequestStatus;
import com.healthStation.ambulanceService.repository.AmbulanceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AmbulanceRequestServiceImpl implements AmbulanceRequestService{
    @Autowired
    AmbulanceRequestRepository ambulanceRequestRepository;

    @Override
    public AmbulanceRequest createRequest(AmbulanceRequest request) {
        request.setStatus(AmbulanceRequestStatus.PENDING);
        request.setRequestedAt(Instant.now());
        return ambulanceRequestRepository.save(request);
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
    public AmbulanceRequest updateRequest(AmbulanceRequest request) {
        if(request.getRequestId()==null || !ambulanceRequestRepository.existsById(request.getRequestId())){
            return null;
        }
        return ambulanceRequestRepository.save(request);
    }
}
