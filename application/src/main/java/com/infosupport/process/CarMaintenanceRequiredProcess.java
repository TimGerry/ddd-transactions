package com.infosupport.process;

import com.infosupport.car.command.MarkCarForMaintenance;
import com.infosupport.car.event.CarMaintenancePrescribed;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

// TODO: remove car maintenance required event and listen to car trip recorded
@Component
@AllArgsConstructor
public class CarMaintenanceRequiredProcess {

    public final ApplicationEventPublisher eventPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onCarMaintenanceRequired(CarMaintenancePrescribed carMaintenancePrescribed) {
        final var markCarForMaintenance = new MarkCarForMaintenance(carMaintenancePrescribed.licensePlate());
        eventPublisher.publishEvent(markCarForMaintenance);
    }
}
