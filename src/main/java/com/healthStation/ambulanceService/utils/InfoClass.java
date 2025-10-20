package com.healthStation.ambulanceService.utils;

import lombok.Getter;

@Getter
public class InfoClass {
    public static final int ambulanceCapacity=1;
    public static final String hospitalLocationEndpoint="http://localhost:8000/hospitalManagement/apis/ambulanceService/hospital/hospitalLocation";
    public static final String hospitalIdEndpoint="http://localhost:8000/hospitalManagement/apis/ambulanceService/hospital/Id/";
}
