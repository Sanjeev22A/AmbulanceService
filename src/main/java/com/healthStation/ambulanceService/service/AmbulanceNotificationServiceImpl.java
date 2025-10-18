package com.healthStation.ambulanceService.service;

import com.healthStation.ambulanceService.model.AmbulanceNotification;
import com.healthStation.ambulanceService.model.AmbulanceNotificationStatusType;
import com.healthStation.ambulanceService.model.AmbulanceRequest;
import com.healthStation.ambulanceService.repository.AmbulanceNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AmbulanceNotificationServiceImpl implements AmbulanceNotificationService{

    @Autowired
    AmbulanceNotificationRepository ambulanceNotificationRepository;

    @Override
    public AmbulanceNotification createNotification(AmbulanceNotification notification) {
        notification.setNotifiedAt(Instant.now());
        return ambulanceNotificationRepository.save(notification);
    }

    @Override
    public Optional<AmbulanceNotification> getNotificationById(Long id) {
        return ambulanceNotificationRepository.findById(id);
    }

    @Override
    public List<AmbulanceNotification> getAllNotifications() {
        return ambulanceNotificationRepository.findAll();
    }

    @Override
    public List<AmbulanceNotification> getNotificationsByRequest(AmbulanceRequest request) {
        return ambulanceNotificationRepository.findByAmbulanceRequest(request);
    }

    @Override
    public List<AmbulanceNotification> getNotificationsByStatus(String status) {
        try{
            AmbulanceNotificationStatusType notificationStatus=AmbulanceNotificationStatusType.valueOf(status.toUpperCase());
            return ambulanceNotificationRepository.findByNotificationStatus(notificationStatus);
        }catch(IllegalArgumentException e){
            return Collections.emptyList();
        }
    }

    @Override
    public AmbulanceNotification updateNotification(AmbulanceNotification notification) {
        if (notification.getId() == null || !ambulanceNotificationRepository.existsById(notification.getId())) {
            throw new IllegalArgumentException("Notification does not exist");
        }

        if (notification.getNotificationStatus() != null) {
            notification.setRespondedAt(Instant.now());
        }
        return ambulanceNotificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        ambulanceNotificationRepository.deleteById(id);
    }
}
