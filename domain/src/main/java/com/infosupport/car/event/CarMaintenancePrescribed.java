package com.infosupport.car.event;

import com.infosupport.car.LicensePlate;
import com.infosupport.common.Event;

public record CarMaintenancePrescribed(
        String licensePlate,
        double distanceSinceLastMaintenance
) implements Event<LicensePlate> {

    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
