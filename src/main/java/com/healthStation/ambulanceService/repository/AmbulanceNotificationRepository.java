package com.healthStation.ambulanceService.repository;

import com.healthStation.ambulanceService.model.AmbulanceNotification;
import com.healthStation.ambulanceService.model.AmbulanceNotificationStatusType;
import com.healthStation.ambulanceService.model.AmbulanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmbulanceNotificationRepository extends JpaRepository<AmbulanceNotification,Long> {

    List<AmbulanceNotification> findByAmbulanceRequest(AmbulanceRequest request);


    List<AmbulanceNotification> findByNotificationStatus(AmbulanceNotificationStatusType status);


    List<AmbulanceNotification> findByAmbulance_AmbulanceId(Long ambulanceId);
}
