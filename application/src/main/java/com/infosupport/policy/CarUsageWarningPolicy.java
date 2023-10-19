package com.infosupport.policy;

import com.infosupport.car.Car;
import com.infosupport.car.CarState;
import com.infosupport.car.LicensePlate;
import com.infosupport.car.event.BrokenCarUsed;
import com.infosupport.common.DomainPolicy;
import com.infosupport.common.Event;

import java.util.ArrayList;
import java.util.List;

public class CarUsageWarningPolicy implements DomainPolicy<Car, LicensePlate> {
    @Override
    public List<Event<LicensePlate>> determineFollowUpEvents(Car car) {
        final var followUpEvents = new ArrayList<Event<LicensePlate>>();

        if (car.getCarState() == CarState.BROKEN) {
            followUpEvents.add(new BrokenCarUsed(
                    car.getId().value(),
                    car.getCarState(),
                    car.getDistanceSinceLastMaintenance())
            );
        }

        return followUpEvents;
    }
}
