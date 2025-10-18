package com.healthStation.ambulanceService.service;

import com.healthStation.ambulanceService.model.AmbulanceAssignment;
import com.healthStation.ambulanceService.model.AmbulanceRequest;
import com.healthStation.ambulanceService.model.AmbulanceServiceType;
import com.healthStation.ambulanceService.repository.AmbulanceAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AmbulanceAssignmentServiceImpl implements AmbulanceAssignmentService{
    @Autowired
    AmbulanceAssignmentRepository ambulanceAssignmentRepository;
    @Override
    public AmbulanceAssignment createAssignment(AmbulanceAssignment ambulanceAssignment) {
        ambulanceAssignment.setAssignedAt(Instant.now());
        return ambulanceAssignmentRepository.save(ambulanceAssignment);
    }

    @Override
    public Optional<AmbulanceAssignment> getAssignmentById(Long id) {
        return ambulanceAssignmentRepository.findById(id);
    }

    @Override
    public List<AmbulanceAssignment> getAllAssignment() {
        return ambulanceAssignmentRepository.findAll();
    }

    @Override
    public List<AmbulanceAssignment> getAssignmentByRequest(AmbulanceRequest request) {
        return ambulanceAssignmentRepository.findAmbulanceRequest(request);
    }

    @Override
    public List<AmbulanceAssignment> getAssignmentByServiceType(String assignmentServiceType) {
        try{
            AmbulanceServiceType ambulanceServiceType=AmbulanceServiceType.valueOf(assignmentServiceType.toUpperCase());
            return ambulanceAssignmentRepository.findByAmbulanceServiceType(ambulanceServiceType);
        }catch(IllegalArgumentException e){
            return Collections.emptyList();
        }
    }

    @Override
    public AmbulanceAssignment updateAssignment(AmbulanceAssignment assignment) {
        if (assignment.getId() == null || !ambulanceAssignmentRepository.existsById(assignment.getId())) {
            throw new IllegalArgumentException("Assignment does not exist");
        }
        return ambulanceAssignmentRepository.save(assignment);
    }

    @Override
    public void deleteAssignment(Long id) {
        ambulanceAssignmentRepository.deleteById(id);
    }
}
