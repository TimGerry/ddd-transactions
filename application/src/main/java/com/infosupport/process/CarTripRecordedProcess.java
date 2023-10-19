package com.infosupport.process;

import com.infosupport.car.CarState;
import com.infosupport.car.command.MarkCarForMaintenance;
import com.infosupport.car.event.CarTripRecorded;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CarTripRecordedProcess {

    private final ApplicationEventPublisher eventPublisher;
    private final double distanceKmUntilMaintenanceNeeded;

    public CarTripRecordedProcess(ApplicationEventPublisher eventPublisher,
                                  @Value("${carApp.policies.distanceKmUntilMaintenanceNeeded}") double distanceKmUntilMaintenanceNeeded) {
        this.eventPublisher = eventPublisher;
        this.distanceKmUntilMaintenanceNeeded = distanceKmUntilMaintenanceNeeded;
    }

    /*
    * The transaction will have been committed already,
    * but the transactional resources might still be active and accessible.
    * As a consequence, any data access code triggered at this point will still “participate” in the original transaction,
    * allowing to perform some cleanup (with no commit following anymore!)
    * */
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    @Async
    public void onCarTripRecorded(CarTripRecorded carTripRecorded) {
        if (carTripRecorded.getCarState() == CarState.BROKEN) {
            log.warn("Trip was recorded for broken car {}", carTripRecorded.getAggregateId());
        }

        if (carTripRecorded.getDistanceKmSinceLastMaintenance() > distanceKmUntilMaintenanceNeeded) {
            eventPublisher.publishEvent(new MarkCarForMaintenance(carTripRecorded.getLicensePlate()));
        }
    }
}
