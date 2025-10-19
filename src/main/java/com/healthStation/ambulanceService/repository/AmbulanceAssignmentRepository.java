package com.healthStation.ambulanceService.repository;

import com.healthStation.ambulanceService.model.AmbulanceAssignment;
import com.healthStation.ambulanceService.model.AmbulanceRequest;
import com.healthStation.ambulanceService.model.AmbulanceServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmbulanceAssignmentRepository extends JpaRepository<AmbulanceAssignment,Long> {

    List<AmbulanceAssignment> findByAmbulanceRequest(AmbulanceRequest request);

    List<AmbulanceAssignment> findByAmbulanceServiceType(AmbulanceServiceType serviceType);

    List<AmbulanceAssignment> findByAssignmentId(Long ambulanceId);
    Boolean existsByAmbulanceRequest_RequestId(Long requestId);
}
