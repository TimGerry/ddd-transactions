package com.infosupport.car.command;

import com.infosupport.car.LicensePlate;
import com.infosupport.common.Command;

public record RemoveCar(String licensePlate) implements Command<LicensePlate> {
    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
