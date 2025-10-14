package com.healthStation.ambulanceService.repository;

import com.healthStation.ambulanceService.model.Ambulance;
import com.healthStation.ambulanceService.model.AmbulanceStatusType;
import com.healthStation.ambulanceService.model.AmbulanceType;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AmbulanceRepository extends JpaRepository<Ambulance,Long> {
    @Query(
            value = "SELECT * FROM ambulance a WHERE ST_DWithin(a.location::geography, :src::geography, :distance)",
            nativeQuery = true
    )
    List<Ambulance> findWithinDistance(@Param("src") Point src, @Param("distance") double distanceInMeters);

    List<Ambulance> findByStatus(AmbulanceStatusType status);
    List<Ambulance> findByType(AmbulanceType type);
}
