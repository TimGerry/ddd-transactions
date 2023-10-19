package com.infosupport.incoming.event;

import com.infosupport.car.CarState;

public record CarCrashedEventDto(String licensePlate, CarState carState) {
}
