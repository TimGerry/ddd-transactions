package com.infosupport.dto;

import com.infosupport.car.CarState;

public record CarDrivenDto(
        String licensePlate,
        double tripDistanceKm,
        CarState carState
) {}
