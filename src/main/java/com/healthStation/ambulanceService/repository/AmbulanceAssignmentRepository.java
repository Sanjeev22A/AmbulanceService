package com.healthStation.ambulanceService.repository;

import com.healthStation.ambulanceService.model.AmbulanceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceAssignmentRepository extends JpaRepository<AmbulanceAssignment,Long> {
}
