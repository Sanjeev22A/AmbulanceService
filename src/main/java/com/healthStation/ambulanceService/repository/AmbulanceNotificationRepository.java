package com.healthStation.ambulanceService.repository;

import com.healthStation.ambulanceService.model.AmbulanceNotification;
import com.healthStation.ambulanceService.model.AmbulanceNotificationStatusType;
import com.healthStation.ambulanceService.model.AmbulanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AmbulanceNotificationRepository extends JpaRepository<AmbulanceNotification,Long> {

    List<AmbulanceNotification> findByAmbulanceRequest(AmbulanceRequest request);


    List<AmbulanceNotification> findByNotificationStatus(AmbulanceNotificationStatusType status);


    List<AmbulanceNotification> findByAmbulance_AmbulanceId(Long ambulanceId);

    //Modifying means Delete or update query and not select query
    @Modifying
    @Query("update AmbulanceNotification n set n.notificationStatus = :status, n.respondedAt = CURRENT_TIMESTAMP where n.ambulance.id = :ambulanceId and n.ambulanceRequest.id = :requestId")
    void updateStatus(@Param("requestId") Long requestId,@Param("ambulanceId") Long ambulanceId,@Param("status") AmbulanceNotificationStatusType notificationStatusType);
}
