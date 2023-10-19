package com.infosupport.car.event;

import com.infosupport.car.LicensePlate;
import com.infosupport.common.Event;

public record CarMarkedForMaintenance(String licensePlate, boolean maintenanceRequired) implements Event<LicensePlate> {

    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
