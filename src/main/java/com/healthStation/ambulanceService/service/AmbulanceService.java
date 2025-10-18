package com.healthStation.ambulanceService.service;

import com.healthStation.ambulanceService.model.Ambulance;
import com.healthStation.ambulanceService.model.AmbulanceStatusType;
import com.healthStation.ambulanceService.model.AmbulanceType;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Optional;

public interface AmbulanceService {
    List<Ambulance> getAllAmbulance();
    Optional<Ambulance> getAmbulanceById(Long id);
    Ambulance saveAmbulance(Ambulance ambulance);
    Ambulance saveAmbulance(Ambulance ambulance,double latitude,double longitude);
    Ambulance updateAmbulance(Long id,Ambulance ambulanceDetails);
    void deleteAmbulance(Long id);


    List<Ambulance> findByStatus(AmbulanceStatusType status);
    List<Ambulance> findByType(AmbulanceType type);
    List<Ambulance> findNearbyAmbulance(int count);
}
