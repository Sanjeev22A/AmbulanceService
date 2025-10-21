package com.healthStation.ambulanceService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name="Ambulance_Notification",
        uniqueConstraints= @UniqueConstraint(columnNames = {"request_id", "ambulance_id"})
)
public class AmbulanceNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notification_id")
    private Long id;

    //Foreign Key
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="request_id",nullable = false)
    private AmbulanceRequest ambulanceRequest;

    //Foreign Key
    @ManyToOne(fetch=FetchType.LAZY) //As one ambulance can get many notifications
    @JoinColumn(name="ambulance_id",nullable = false)
    private Ambulance ambulance;

    @Column(name="notified_at",columnDefinition = "TIMESTAMPTZ")
    private Instant notifiedAt;

    @Column(name="responded_at",columnDefinition = "TIMESTAMPTZ")
    private Instant respondedAt;

    @Column(name="response_status")
    @Enumerated(EnumType.STRING)
    private AmbulanceNotificationStatusType notificationStatus;

    @Version
    private Long version;
}
