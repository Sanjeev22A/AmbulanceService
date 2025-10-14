package com.healthStation.ambulanceService.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Data
@Entity
@Table(name="Ambulance_Request")
public class AmbulanceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="request_id")
    private Long requestId;
    //This is a foreign key component but hasnt been implemented yet
    //When implemented uncomment the below line
    /*
    @ManyToOne(fetch=FetchType.LAZY) // One patient can have multiple request for ambulance
    @JoinColumn(name="patient_id",nullable=false)
    private Patient patient;
     */
    @Column(name="patient_id")
    private Long patientId;

    @Column(name = "pickup_location", columnDefinition = "geometry(Point,4326)")
    private Point pickupLocation;

    @Column(name="destination_location",columnDefinition = "geometry(Point,4326)")
    private Point destinationLocation;

    //This is a foreign key but not yet thought about a table for hospital, so it is kept at hold
    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="destination_hospital_id")
    private Hospital destinationHospital;
     */
    //Use the above if accepted as a table for hospital
    //Else use the below
    /*
    private String destHospName
     */
    @Column(name="destination_hospital_id")
    private Long destHospId;

    @Enumerated(EnumType.STRING)
    private AmbulanceRequestStatus status;

    @Column(name="requested_at",columnDefinition = "TIMESTAMPTZ")
    private Instant requestedAt;

    @Column(name="accepted_at",columnDefinition = "TIMESTAMPTZ")
    private Instant acceptedAt;

    @Column(name="completed_at", columnDefinition = "TIMESTAMPTZ")
    private Instant completedAt;

    @Version
    private Long version;
}
