package com.infosupport.process;

import com.infosupport.car.command.MarkCarForMaintenance;
import com.infosupport.car.event.CarMaintenancePrescribed;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class CarMaintenancePrescribedProcess {

    public final ApplicationEventPublisher eventPublisher;

    /*
    * The transaction will have been committed already,
    * but the transactional resources might still be active and accessible.
    * As a consequence, any data access code triggered at this point will still “participate” in the original transaction,
    * allowing to perform some cleanup (with no commit following anymore!)
    * */
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    @Async
    public void onCarMaintenanceRequired(CarMaintenancePrescribed carMaintenancePrescribed) {
        final var markCarForMaintenance = new MarkCarForMaintenance(carMaintenancePrescribed.licensePlate());
        eventPublisher.publishEvent(markCarForMaintenance);
    }
}
