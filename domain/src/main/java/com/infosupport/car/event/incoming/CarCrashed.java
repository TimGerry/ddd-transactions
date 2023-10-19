package com.infosupport.car.event.incoming;

import com.infosupport.car.CarState;
import com.infosupport.car.LicensePlate;
import com.infosupport.common.IncomingEvent;

public record CarCrashed(String licensePlate, CarState carState) implements IncomingEvent<LicensePlate> {

    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
