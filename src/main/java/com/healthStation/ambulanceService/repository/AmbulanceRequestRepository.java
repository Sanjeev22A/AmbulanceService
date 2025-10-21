package com.healthStation.ambulanceService.repository;
import java.util.List;
import java.util.Optional;

import com.healthStation.ambulanceService.model.AmbulanceRequest;
import com.healthStation.ambulanceService.model.AmbulanceRequestStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AmbulanceRequestRepository extends JpaRepository<AmbulanceRequest,Long> {
    List<AmbulanceRequest> findByPatientId(Long patientId);
    List<AmbulanceRequest> findByStatus(AmbulanceRequestStatus status);

    //We must have an atomic fetch for update, so we create the db level atomic fetch here with locks,This lock is for writing
    //Dont think too much about the query style, its just an alias used
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from AmbulanceRequest r where r.id = :id")
    Optional<AmbulanceRequest> findByIdForUpdate(@Param("id") Long requestId);
}
