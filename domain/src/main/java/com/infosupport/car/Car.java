package com.infosupport.car;

import com.infosupport.car.command.MarkCarForMaintenance;
import com.infosupport.car.command.RecordCarTrip;
import com.infosupport.car.command.RegisterCar;
import com.infosupport.car.command.RemoveCar;
import com.infosupport.car.event.CarMarkedForMaintenance;
import com.infosupport.car.event.CarRegistered;
import com.infosupport.car.event.CarRemoved;
import com.infosupport.car.event.CarTripRecorded;
import com.infosupport.car.rule.MarkableForMaintenanceRule;
import com.infosupport.common.AggregateRoot;
import com.infosupport.common.Command;
import com.infosupport.common.DomainEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Car extends AggregateRoot<LicensePlate> {

    private String brand;
    private String model;
    private double distanceKm;
    private double distanceKmSinceLastMaintenance;
    private CarState carState;
    private boolean maintenanceRequired;

    public Car(LicensePlate id, int version) {
        super(id, version);
    }

    @Override
    public void apply(Command<LicensePlate> command) {
        log.info("Applying command to aggregate with id {}", getId());
        switch (command) {
            case RegisterCar registerCar -> apply(registerCar);
            case RecordCarTrip recordCarTrip -> apply(recordCarTrip);
            case MarkCarForMaintenance markCarForMaintenance -> apply(markCarForMaintenance);
            case RemoveCar removeCar -> apply(removeCar);
            default -> throw new UnsupportedOperationException("Command is not supported for Car aggregate: " + command.getClass());
        }
    }

    private void apply(RegisterCar registerCar) {
        final var carRegistered = new CarRegistered(
                registerCar.licensePlate(),
                registerCar.brand(),
                registerCar.model(),
                getVersion() + 1
        );

        raiseEvent(carRegistered);
    }

    private void apply(RecordCarTrip recordCarTrip) {
        final var carTripRecorded = new CarTripRecorded(
                recordCarTrip.licensePlate(),
                recordCarTrip.tripDistanceKm(),
                distanceKmSinceLastMaintenance + recordCarTrip.tripDistanceKm(),
                recordCarTrip.carState(),
                getVersion() + 1
        );

        raiseEvent(carTripRecorded);
    }

    private void apply(MarkCarForMaintenance markCarForMaintenance) {
        if (!MarkableForMaintenanceRule.applies(this)) return;

        final var carMarkedForMaintenance = new CarMarkedForMaintenance(
                markCarForMaintenance.licensePlate(),
                true,
                getVersion() + 1
        );

        raiseEvent(carMarkedForMaintenance);
    }

    private void apply(RemoveCar removeCar) {
        final var carRemoved = new CarRemoved(
                removeCar.licensePlate(),
                removeCar.carState(),
                getVersion() + 1
        );

        raiseEvent(carRemoved);
    }

    @Override
    protected void handle(DomainEvent<LicensePlate> domainEvent) {
        log.info("Handling event of type {} for aggregate with id {}", domainEvent.getClass(), getId());
        switch (domainEvent) {
            case CarRegistered carRegistered -> handle(carRegistered);
            case CarTripRecorded carTripRecorded -> handle(carTripRecorded);
            case CarMarkedForMaintenance carMarkedForMaintenance -> handle(carMarkedForMaintenance);
            case CarRemoved carRemoved -> handle(carRemoved);
            default -> throw new UnsupportedOperationException("Event is not supported for Car aggregate: " + domainEvent.getClass());
        }
    }

    private void handle(CarTripRecorded carTripRecorded) {
        this.distanceKm += carTripRecorded.getTripDistanceKm();
        this.distanceKmSinceLastMaintenance = carTripRecorded.getDistanceKmSinceLastMaintenance();
        this.carState = carTripRecorded.getCarState();
    }

    private void handle(CarRegistered carRegistered) {
        this.brand = carRegistered.getBrand();
        this.model = carRegistered.getModel();
        this.carState = CarState.NEW;
    }

    private void handle(CarMarkedForMaintenance carMarkedForMaintenance) {
        this.maintenanceRequired = carMarkedForMaintenance.isMaintenanceRequired();
    }

    private void handle(CarRemoved carRemoved) {
        this.carState = carRemoved.getCarstate();
    }
}
