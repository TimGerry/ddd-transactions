package com.infosupport.car.event;

import com.infosupport.car.CarState;
import com.infosupport.car.LicensePlate;
import com.infosupport.common.Event;

public record CarRemoved(String licensePlate, CarState carstate) implements Event<LicensePlate> {
    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
