package com.healthStation.ambulanceService.service;

import com.healthStation.ambulanceService.model.AmbulanceAssignment;
import com.healthStation.ambulanceService.model.AmbulanceRequest;

import java.util.List;
import java.util.Optional;

public interface AmbulanceAssignmentService {

    AmbulanceAssignment createAssignment(AmbulanceAssignment ambulanceAssignment);

    Optional<AmbulanceAssignment> getAssignmentById(Long id);
    List<AmbulanceAssignment> getAllAssignment();
    List<AmbulanceAssignment> getAssignmentByRequest(AmbulanceRequest request);

    List<AmbulanceAssignment> getAssignmentByServiceType(String assignmentServiceType);
    AmbulanceAssignment updateAssignment(AmbulanceAssignment assignment);
    void deleteAssignment(Long id);
    AmbulanceAssignment assignAmbulance(Long ambulanceId,Long requestId);
}
