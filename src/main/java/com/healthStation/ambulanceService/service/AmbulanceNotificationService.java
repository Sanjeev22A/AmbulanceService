package com.healthStation.ambulanceService.service;

import com.healthStation.ambulanceService.model.AmbulanceNotification;
import com.healthStation.ambulanceService.model.AmbulanceNotificationStatusType;
import com.healthStation.ambulanceService.model.AmbulanceRequest;

import java.util.List;
import java.util.Optional;

public interface AmbulanceNotificationService {
    AmbulanceNotification createNotification(AmbulanceNotification notification);

    Optional<AmbulanceNotification> getNotificationById(Long id);

    List<AmbulanceNotification> getAllNotifications();

    List<AmbulanceNotification> getNotificationsByRequest(AmbulanceRequest request);

    List<AmbulanceNotification> getNotificationsByStatus(String status);

    AmbulanceNotification updateNotification(AmbulanceNotification notification);

    void deleteNotification(Long id);
}
