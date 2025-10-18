package com.healthStation.ambulanceService.repository;
import java.util.List;
import com.healthStation.ambulanceService.model.AmbulanceRequest;
import com.healthStation.ambulanceService.model.AmbulanceRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceRequestRepository extends JpaRepository<AmbulanceRequest,Long> {
    List<AmbulanceRequest> findByPatientId(Long patientId);
    List<AmbulanceRequest> findByStatus(AmbulanceRequestStatus status);
}
