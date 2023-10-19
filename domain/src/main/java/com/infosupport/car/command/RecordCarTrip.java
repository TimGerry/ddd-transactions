package com.infosupport.car.command;

import com.infosupport.car.CarState;
import com.infosupport.car.LicensePlate;
import com.infosupport.common.Command;


public record RecordCarTrip(
        String licensePlate,
        double tripDistanceKm,
        CarState carState
) implements Command<LicensePlate> {

    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
