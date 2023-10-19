package com.infosupport.car.event;

import com.infosupport.car.CarState;
import com.infosupport.car.LicensePlate;
import com.infosupport.common.Event;

public record CarTripRecorded(
        String licensePlate,
        double tripDistanceKm,
        double distanceKmSinceLastMaintenance,
        CarState carState
) implements Event<LicensePlate> {

    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
