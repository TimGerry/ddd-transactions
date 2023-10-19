package com.infosupport.process;

import com.infosupport.car.CarState;
import com.infosupport.car.LicensePlate;
import com.infosupport.car.command.MarkCarForMaintenance;
import com.infosupport.car.command.RemoveCar;
import com.infosupport.car.event.CarCrashed;
import com.infosupport.common.Command;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class CarCrashedProcess {

    public final ApplicationEventPublisher eventPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onCarCrashed(CarCrashed carCrashed) {
        final Command<LicensePlate> resultingCommand;

        if (carCrashed.carState() == CarState.TOTALED) {
            resultingCommand = new RemoveCar(carCrashed.licensePlate());
        } else {
            resultingCommand = new MarkCarForMaintenance(carCrashed.licensePlate());
        }

        eventPublisher.publishEvent(resultingCommand);
    }
}
