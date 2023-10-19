package com.infosupport.process;

import com.infosupport.car.command.MarkCarForMaintenance;
import com.infosupport.car.event.CarMaintenancePrescribed;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarMaintenancePrescribedProcess {

    public final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void onCarMaintenanceRequired(CarMaintenancePrescribed carMaintenancePrescribed) {
        final var markCarForMaintenance = new MarkCarForMaintenance(carMaintenancePrescribed.licensePlate());
        eventPublisher.publishEvent(markCarForMaintenance);
    }
}
