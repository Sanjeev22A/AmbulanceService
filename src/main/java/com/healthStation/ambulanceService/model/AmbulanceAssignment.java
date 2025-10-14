package com.healthStation.ambulanceService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name="Ambulance_Assignment")
public class AmbulanceAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="assignment_id")
    private Long id;

    //Foreign key
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="request_id",nullable = false)
    private AmbulanceRequest ambulanceRequest;

    //Foreign Key
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ambulance_id",nullable=false)
    private Ambulance ambulance;

    @Column(name="assigned_at",columnDefinition = "TIMESTAMPTZ")
    private Instant assignedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private AmbulanceServiceType ambulanceServiceType;

    //This version field ensures consistency and atomicity of operations, especially when we are doing real time ops
    @Version
    private Long version;

}
