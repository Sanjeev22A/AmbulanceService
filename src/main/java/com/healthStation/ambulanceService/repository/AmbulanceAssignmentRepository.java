package com.healthStation.ambulanceService.repository;

import com.healthStation.ambulanceService.model.AmbulanceAssignment;
import com.healthStation.ambulanceService.model.AmbulanceRequest;
import com.healthStation.ambulanceService.model.AmbulanceServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmbulanceAssignmentRepository extends JpaRepository<AmbulanceAssignment,Long> {

    List<AmbulanceAssignment> findAmbulanceRequest(AmbulanceRequest request);

    List<AmbulanceAssignment> findByAmbulanceServiceType(AmbulanceServiceType serviceType);

    List<AmbulanceAssignment> findByAmbulanceId(Long ambulanceId);
}
