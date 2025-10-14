package com.healthStation.ambulanceService.controller;

import com.healthStation.ambulanceService.model.Ambulance;
import com.healthStation.ambulanceService.service.AmbulanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apis/test")
public class TestController {

    @Autowired
    AmbulanceService ambulanceService;

    @GetMapping("/all")
    public List<Ambulance> getAllAmbulance(){
        return ambulanceService.getAllAmbulance();
    }

    @GetMapping("/nearby/{count}")
    public List<Ambulance> getNearbyAmbulance(@PathVariable Long count){
        return null;
        //Ahh i forgot to think about the source location , let me get back to it later
        //return ambulanceService.findNearbyAmbulance(count);
    }

}
