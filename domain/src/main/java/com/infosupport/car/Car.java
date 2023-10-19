package com.infosupport.car;

import com.infosupport.car.command.MarkCarForMaintenance;
import com.infosupport.car.command.RecordCarTrip;
import com.infosupport.car.command.RegisterCar;
import com.infosupport.car.command.RemoveCar;
import com.infosupport.car.event.BrokenCarUsed;
import com.infosupport.car.event.CarMarkedForMaintenance;
import com.infosupport.car.event.CarRegistered;
import com.infosupport.car.event.CarRemoved;
import com.infosupport.car.event.CarTripRecorded;
import com.infosupport.car.rule.MarkableForMaintenanceRule;
import com.infosupport.car.rule.TripWarningNeededRule;
import com.infosupport.common.AggregateRoot;
import com.infosupport.common.Command;
import com.infosupport.common.Event;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Car extends AggregateRoot<LicensePlate> {

    private String brand;
    private String model;
    private double distanceKm;
    private double distanceSinceLastMaintenance;
    private CarState carState;
    private boolean maintenanceRequired;

    public Car(LicensePlate id) {
        super(id);
    }

    @Override
    public void apply(Command<LicensePlate> command) {
        log.info("Applying command to aggregate with id {}", getId());
        switch (command) {
            case RegisterCar registerCar -> apply(registerCar);
            case RecordCarTrip recordCarTrip -> apply(recordCarTrip);
            case MarkCarForMaintenance markCarForMaintenance -> apply(markCarForMaintenance);
            case RemoveCar removeCar -> apply(removeCar);
            default -> log.info("No need to handle command of type {}", command.getClass());
        }
    }

    private void apply(RegisterCar registerCar) {
        final var carRegistered = new CarRegistered(
                registerCar.licensePlate(),
                registerCar.brand(),
                registerCar.model()
        );

        raiseEvent(carRegistered);
    }

    private void apply(RecordCarTrip recordCarTrip) {
        final var carTripRecorded = new CarTripRecorded(
                recordCarTrip.licensePlate(),
                recordCarTrip.tripDistanceKm(),
                recordCarTrip.carState()
        );

        raiseEvent(carTripRecorded);

        if (!TripWarningNeededRule.applies(this)) return;

        final var brokenCarUsed = new BrokenCarUsed(
                recordCarTrip.licensePlate(),
                carState,
                distanceSinceLastMaintenance
        );

        raiseEvent(brokenCarUsed);
    }

    private void apply(MarkCarForMaintenance markCarForMaintenance) {
        if (!MarkableForMaintenanceRule.applies(this)) return;

        final var carMarkedForMaintenance = new CarMarkedForMaintenance(
                markCarForMaintenance.licensePlate(),
                true
        );

        raiseEvent(carMarkedForMaintenance);
    }

    private void apply(RemoveCar removeCar) {
        final var carRemoved = new CarRemoved(
                removeCar.licensePlate(),
                carState
        );

        raiseEvent(carRemoved);
    }

    @Override
    protected void handle(Event<LicensePlate> event) {
        log.info("Handling event of type {} for aggregate with id {}", event.getClass(), getId());
        switch (event) {
            case CarRegistered carRegistered -> handle(carRegistered);
            case CarTripRecorded carTripRecorded -> handle(carTripRecorded);
            case CarMarkedForMaintenance carMarkedForMaintenance -> handle(carMarkedForMaintenance);
            case CarRemoved carRemoved -> handle(carRemoved);
            default -> log.info("No need to handle event of type {}", event.getClass());
        }
    }

    private void handle(CarTripRecorded carTripRecorded) {
        this.distanceKm += carTripRecorded.tripDistanceKm();
        this.distanceSinceLastMaintenance += carTripRecorded.tripDistanceKm();
        this.carState = carTripRecorded.carState();
    }

    private void handle(CarRegistered carRegistered) {
        this.brand = carRegistered.brand();
        this.model = carRegistered.model();
        this.carState = CarState.NEW;
    }

    private void handle(CarMarkedForMaintenance carMarkedForMaintenance) {
        this.maintenanceRequired = carMarkedForMaintenance.maintenanceRequired();
    }

    private void handle(CarRemoved carRemoved) {
        this.carState = carRemoved.carstate();
    }
}
