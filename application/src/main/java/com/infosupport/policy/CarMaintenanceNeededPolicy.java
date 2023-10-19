package com.infosupport.policy;

import com.infosupport.car.Car;
import com.infosupport.car.LicensePlate;
import com.infosupport.car.event.CarMaintenancePrescribed;
import com.infosupport.common.DomainPolicy;
import com.infosupport.common.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CarMaintenanceNeededPolicy implements DomainPolicy<Car, LicensePlate> {

    private final double distanceKmUntilMaintenanceNeeded;

    public CarMaintenanceNeededPolicy(
            @Value("${carApp.policies.distanceKmUntilMaintenanceNeeded}") double distanceKmUntilMaintenanceNeeded
    ) {
        this.distanceKmUntilMaintenanceNeeded = distanceKmUntilMaintenanceNeeded;
    }

    public List<Event<LicensePlate>> determineFollowUpEvents(Car car) {
        final var followUpEvents = new ArrayList<Event<LicensePlate>>();

        if (!car.isMaintenanceRequired() && car.getDistanceSinceLastMaintenance() >= distanceKmUntilMaintenanceNeeded) {
            followUpEvents.add(new CarMaintenancePrescribed(car.getId().value(), car.getDistanceSinceLastMaintenance()));
        }

        return followUpEvents;
    }
}
