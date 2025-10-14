package com.healthStation.ambulanceService.repository;

import com.healthStation.ambulanceService.model.AmbulanceNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceNotificationRepository extends JpaRepository<AmbulanceNotification,Long> {
}
