package com.healthStation.ambulanceService.service;
import com.healthStation.ambulanceService.utils.Deserializer;
import com.healthStation.ambulanceService.utils.InfoClass;
import com.healthStation.ambulanceService.utils.Location;
import org.locationtech.jts.geom.*;
import com.healthStation.ambulanceService.model.Ambulance;
import com.healthStation.ambulanceService.model.AmbulanceStatusType;
import com.healthStation.ambulanceService.model.AmbulanceType;
import com.healthStation.ambulanceService.repository.AmbulanceRepository;
import com.healthStation.ambulanceService.utils.MyConfig;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AmbulanceServiceImpl implements AmbulanceService{
    ///This will act as a default argument for the ambulance
    private final int ambulanceCapacity= InfoClass.ambulanceCapacity;
    ///This is the location creator for this service with floating point precision model and WGS 84 coordinate system
    private final GeometryFactory geometryFactory=new GeometryFactory(new PrecisionModel(),4326);

    @Autowired
    private AmbulanceRepository ambulanceRepository;

    private final RestTemplate restTemplate= MyConfig.restTemplate();

    public List<Ambulance> getAllAmbulance(){
        return ambulanceRepository.findAll();
    }


    @Override
    public Optional<Ambulance> getAmbulanceById(Long id) {
        return ambulanceRepository.findById(id);
    }

    @Transactional
    @Override
    public Ambulance saveAmbulance(Ambulance ambulance) {
        try {
            if (ambulance.getCapacity() == 0) {
                ambulance.setCapacity(ambulanceCapacity);
            }
            if (ambulance.getLocation() == null) {
                String t = MyConfig.restTemplate().getForObject(InfoClass.hospitalLocationEndpoint, String.class);
                Point p = Deserializer.deserialize(t);
                com.healthStation.ambulanceService.utils.Location loc = new Location(p);
                ambulance.setLocation(loc.getHospitalCoordinate());
            }
            if(ambulance.getType()==null){
                ambulance.setType(AmbulanceType.BASIC);
            }
            if(ambulance.getStatus()==null){
                ambulance.setStatus(AmbulanceStatusType.AVAILABLE);
            }
            //We will use the server side timestamp for the update time - for consistency and prevents tampering
            ambulance.setUpdatedAt(Instant.now());

            return ambulanceRepository.save(ambulance);
        } catch (Exception e) {

            return null;
        }
    }

    @Transactional
    @Override
    public Ambulance saveAmbulance(Ambulance ambulance,double latitude,double longitude){

        if(ambulance.getCapacity()==0){
            ambulance.setCapacity(ambulanceCapacity);
        }
        if(ambulance.getType()==null){
            ambulance.setType(AmbulanceType.BASIC);
        }
        if(ambulance.getStatus()==null){
            ambulance.setStatus(AmbulanceStatusType.AVAILABLE);
        }
        Point location=geometryFactory.createPoint(new Coordinate(latitude,longitude));
        ambulance.setLocation(location);
        ambulance.setUpdatedAt(Instant.now());
        System.out.println("Invoking save in repo");
        Ambulance temp=ambulanceRepository.save(ambulance);
        System.out.println("Saved");
        return temp;

    }

    @Transactional
    @Override
    public Ambulance updateAmbulance(Long id, Ambulance ambulanceDetails) {

        return ambulanceRepository.findById(id).map(ambulance -> {
            ambulance.setUpdatedAt(Instant.now());
            ambulance.setStatus(ambulanceDetails.getStatus());
            ambulance.setType(ambulanceDetails.getType());
            ambulance.setCapacity(ambulanceDetails.getCapacity());
            ambulance.setLocation(ambulanceDetails.getLocation());
            ambulance.setDriverId(ambulanceDetails.getDriverId());
            return ambulanceRepository.save(ambulance);
        }).orElseThrow(() -> new RuntimeException("Ambulance not found with id " + id));
    }

    @Transactional
    @Override
    public Boolean deleteAmbulance(Long id) {
        if(!ambulanceRepository.existsById(id)){
            return false;
        }
        ambulanceRepository.deleteById(id);
        return !ambulanceRepository.existsById(id);
    }

    @Override
    public List<Ambulance> findByStatus(AmbulanceStatusType status) {
        return ambulanceRepository.findByStatus(status);
    }

    @Override
    public List<Ambulance> findByType(AmbulanceType type) {
        return ambulanceRepository.findByType(type);
    }

    @Override
    public List<Ambulance> findNearbyAmbulance(int count){
        try {
            String t = restTemplate.getForObject(InfoClass.hospitalLocationEndpoint, String.class);
            Point src = Deserializer.deserialize(t);
            return findNearbyAmbulance(src, count);
        } catch (Exception e) {
            return null;
        }
    }
    private List<Ambulance> findNearbyAmbulance(Point src,int count) {
        //Here the number of ambulance in the particular distance is we increment distance and repeat it until the number of ambulance is greater than count, then we return the count closest ambulances
        long distanceInMeters=500;
        List<Ambulance> nearby=new ArrayList<>();
        while(nearby.size()<count){
            nearby=ambulanceRepository.findWithinDistance(src,distanceInMeters);
            distanceInMeters*=2;
            if(distanceInMeters>50000){
                //An ambulance cant travel more than 50Km to get a patient
                break;
            }
        }
        nearby.sort((a1,a2)->{
            double d1=a1.getLocation().distance(src);
            double d2=a2.getLocation().distance(src);
            return Double.compare(d1,d2);
        });
        return nearby.size()>count? nearby.subList(0,count):nearby;


    }

    @Override
    public Ambulance registerAmbulance(Ambulance ambulance){

        try{

            if(ambulance.getAmbulanceId()!=null && ambulance.getAmbulanceId()!=0) {
                Optional<Ambulance> holder = this.getAmbulanceById(ambulance.getAmbulanceId());
                if (holder.isEmpty()) {

                    return saveAmbulance(ambulance);
                }
                return getDummyAmbulance("The ambulance already exist, so cant register");
            }else{

                return saveAmbulance(ambulance);
            }

        } catch (Exception e) {

            System.out.println("Error when registering");
            return null;
        }
    }

    // Dummy object for testing
    private Ambulance getDummyAmbulance(String errorType) {
        Ambulance dummy = new Ambulance();
        dummy.setAmbulanceId(-1L);
        dummy.setDriverId(0L);
        dummy.setCapacity(0);
        dummy.setType(AmbulanceType.BASIC);
        dummy.setStatus(AmbulanceStatusType.OFFLINE);
        dummy.setUpdatedAt(Instant.now());

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        dummy.setLocation(geometryFactory.createPoint(new Coordinate(0.0, 0.0)));

        System.out.println("Returning dummy ambulance due to error: " + errorType);
        return dummy;
    }


}
