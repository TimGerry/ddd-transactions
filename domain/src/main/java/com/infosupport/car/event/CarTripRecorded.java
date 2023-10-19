package com.infosupport.car.event;

import com.infosupport.car.CarState;
import com.infosupport.car.LicensePlate;
import com.infosupport.common.DomainEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarTripRecorded extends DomainEvent<LicensePlate> {

    private String licensePlate;
    private double tripDistanceKm;
    private double distanceKmSinceLastMaintenance;
    private CarState carState;

    public CarTripRecorded(String licensePlate, double tripDistanceKm, double distanceKmSinceLastMaintenance, CarState carState, int version) {
        super(version);
        this.licensePlate = licensePlate;
        this.tripDistanceKm = tripDistanceKm;
        this.distanceKmSinceLastMaintenance = distanceKmSinceLastMaintenance;
        this.carState = carState;
    }

    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
