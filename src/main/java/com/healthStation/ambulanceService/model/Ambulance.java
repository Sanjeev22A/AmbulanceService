package com.healthStation.ambulanceService.model;

import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.Point;


import java.time.Instant;

@Data
@Entity
@Table(name="Ambulance")
public class Ambulance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ambulance_id")
    private long ambulanceId;

    //This column must actually be a Foreign key
    //When Implementing the rest of the class do the following
    /*
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="driver_id",nullable=false) //Foreign key column
    private Driver driver;
     */
    @Column(name="driver_id")
    private Long driverId;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    @Enumerated(EnumType.STRING)
    private AmbulanceType type;

    @Enumerated(EnumType.STRING)
    private AmbulanceStatusType status;

    private int capacity;

    @Column(name="updated_at", columnDefinition = "TIMESTAMPTZ")
    private Instant updatedAt;

    @Version
    private Long version;

}
