package com.healthStation.ambulanceService.repository;

import com.healthStation.ambulanceService.model.AmbulanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceRequestRepository extends JpaRepository<AmbulanceRequest,Long> {
}
