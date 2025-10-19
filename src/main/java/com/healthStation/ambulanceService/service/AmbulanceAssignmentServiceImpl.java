package com.healthStation.ambulanceService.service;

import com.healthStation.ambulanceService.Exceptions.AmbulanceNotAvailableException;
import com.healthStation.ambulanceService.Exceptions.AmbulanceRequestInvalidException;
import com.healthStation.ambulanceService.model.*;
import com.healthStation.ambulanceService.repository.AmbulanceAssignmentRepository;
import com.healthStation.ambulanceService.repository.AmbulanceRepository;
import com.healthStation.ambulanceService.repository.AmbulanceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AmbulanceAssignmentServiceImpl implements AmbulanceAssignmentService{
    @Autowired
    AmbulanceAssignmentRepository ambulanceAssignmentRepository;
    @Autowired
    AmbulanceRepository ambulanceRepository;
    @Autowired
    AmbulanceRequestRepository ambulanceRequestRepository;
    @Override
    public AmbulanceAssignment createAssignment(AmbulanceAssignment ambulanceAssignment) {
        ambulanceAssignment.setAssignedAt(Instant.now());
        return ambulanceAssignmentRepository.save(ambulanceAssignment);
    }

    @Override
    public Optional<AmbulanceAssignment> getAssignmentById(Long id) {
        return ambulanceAssignmentRepository.findById(id);
    }

    @Override
    public List<AmbulanceAssignment> getAllAssignment() {
        return ambulanceAssignmentRepository.findAll();
    }

    @Override
    public List<AmbulanceAssignment> getAssignmentByRequest(AmbulanceRequest request) {
        return ambulanceAssignmentRepository.findByAmbulanceRequest(request);
    }

    @Override
    public List<AmbulanceAssignment> getAssignmentByServiceType(String assignmentServiceType) {
        try{
            AmbulanceServiceType ambulanceServiceType=AmbulanceServiceType.valueOf(assignmentServiceType.toUpperCase());
            return ambulanceAssignmentRepository.findByAmbulanceServiceType(ambulanceServiceType);
        }catch(IllegalArgumentException e){
            return Collections.emptyList();
        }
    }

    @Override
    public AmbulanceAssignment updateAssignment(AmbulanceAssignment assignment) {
        if (assignment.getAssignmentId() == null || !ambulanceAssignmentRepository.existsById(assignment.getAssignmentId())) {
            throw new IllegalArgumentException("Assignment does not exist");
        }
        return ambulanceAssignmentRepository.save(assignment);
    }

    @Override
    public void deleteAssignment(Long id) {
        ambulanceAssignmentRepository.deleteById(id);
    }

    @Override
    public AmbulanceAssignment assignAmbulance(Long ambulanceId, Long requestId) {
        try {
            Ambulance ambulance = ambulanceRepository.findById(ambulanceId)
                    .orElseThrow(() -> new IllegalArgumentException("Ambulance does not exist"));

            if (ambulance.getStatus() != AmbulanceStatusType.AVAILABLE) {
                throw new AmbulanceNotAvailableException("Ambulance isn't available");
            }

            AmbulanceRequest ambulanceRequest = ambulanceRequestRepository.findById(requestId)
                    .orElseThrow(() -> new IllegalArgumentException("Request does not exist"));

            if (ambulanceRequest.getStatus() != AmbulanceRequestStatus.PENDING) {
                throw new AmbulanceRequestInvalidException(
                        "The request isn't active, it is either accepted or completed!"
                );
            }

            // Create the assignment
            AmbulanceAssignment assignment = new AmbulanceAssignment();
            assignment.setAmbulance(ambulance);
            assignment.setAmbulanceRequest(ambulanceRequest);
            assignment.setAssignedAt(Instant.now());
            assignment.setAmbulanceServiceType(AmbulanceServiceType.ONGOING);

            AmbulanceAssignment savedAssignment = ambulanceAssignmentRepository.save(assignment);

            // Update request status to ACCEPTED
            ambulanceRequest.setStatus(AmbulanceRequestStatus.ACCEPTED);
            ambulanceRequestRepository.save(ambulanceRequest);

            return savedAssignment;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
