package com.healthStation.ambulanceService.repository;

import com.healthStation.ambulanceService.model.Ambulance;
import com.healthStation.ambulanceService.model.AmbulanceServiceType;
import com.healthStation.ambulanceService.model.AmbulanceStatusType;
import com.healthStation.ambulanceService.model.AmbulanceType;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AmbulanceRepository extends JpaRepository<Ambulance,Long> {
    @Query(
            value = "SELECT * FROM ambulance a " +
                    "WHERE ST_DWithin(a.location::geography, CAST(:src AS geography), :distance) " +
                    "AND a.type = CAST(:type AS VARCHAR) " +
                    "AND a.status = CAST(:status AS VARCHAR)",
            nativeQuery = true
    )
    List<Ambulance> findWithinDistance(
            @Param("src") Point src,
            @Param("distance") double distanceInMeters,
            @Param("type") AmbulanceType serviceType,
            @Param("status") AmbulanceStatusType statusType
    );


    List<Ambulance> findByStatus(AmbulanceStatusType status);
    List<Ambulance> findByType(AmbulanceType type);
    List<Ambulance> findByDriverId(Long driverId);
}
