package com.infosupport.handler;

import com.infosupport.car.Car;
import com.infosupport.car.LicensePlate;
import com.infosupport.common.Command;
import com.infosupport.common.CreateCommand;
import com.infosupport.persistence.CarAggregateService;
import com.infosupport.persistence.CarEventStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class CarCommandHandler {

    private final CarEventStore eventStore;
    private final CarAggregateService aggregateService;
    private final ApplicationEventPublisher publisher;

    @EventListener
    /*
      Transactional with propagation REQUIRED (default behaviour)
      This allows a new transaction to be started if a command was directly sent from messaging,
      or an existing transaction to be used if called from a process

      EXPLANATION
       - use spring transactions
       - each aggregate state should be valid after each command, so place @Transactional on command handler
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(Command<LicensePlate> carCommand) {
        log.info("Handling command of type {} for aggregate with id {}", carCommand.getClass(), carCommand.getAggregateId());

        final Car car;

        if (carCommand instanceof CreateCommand<LicensePlate>) {
            car = aggregateService.createCarAggregate(carCommand.getAggregateId());
        } else {
            car = aggregateService.getCarAggregate(carCommand.getAggregateId());
            car.replay(eventStore.getEvents(car.getId()));
        }

        car.apply(carCommand);
        processEvents(car);

        log.info("Succesfully handled command of type {} for aggregate with id {}", carCommand.getClass(), carCommand.getAggregateId());
    }

    private void processEvents(Car car) {
        aggregateService.incrementCarAggregateVersion(car.getId(), car.getVersion(), car.getDomainEvents().size());
        eventStore.addEvents(car.getDomainEvents());
        car.getDomainEvents().forEach(publisher::publishEvent);
    }
}
