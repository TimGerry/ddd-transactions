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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
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
