package com.infosupport.car.command;

import com.infosupport.car.LicensePlate;
import com.infosupport.common.CreateCommand;

public record RegisterCar(
        String licensePlate,
        String brand,
        String model
) implements CreateCommand<LicensePlate> {
    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
