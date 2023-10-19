package com.infosupport.car.event;

import com.infosupport.car.LicensePlate;
import com.infosupport.common.Event;

public record CarRegistered(String licensePlate,
                            String brand,
                            String model
) implements Event<LicensePlate> {

    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
