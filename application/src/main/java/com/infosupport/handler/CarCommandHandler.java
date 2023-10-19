package com.infosupport.handler;

import com.infosupport.car.Car;
import com.infosupport.car.LicensePlate;
import com.infosupport.common.Command;
import com.infosupport.common.CreateCommand;
import com.infosupport.common.DomainPolicy;
import com.infosupport.common.Event;
import com.infosupport.exception.DuplicateException;
import com.infosupport.exception.NotFoundException;
import com.infosupport.persistence.CarEventStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CarCommandHandler {

    private final CarEventStore eventStore;
    private final List<DomainPolicy<Car, LicensePlate>> domainPolicies;
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

        final var car = carCommand instanceof CreateCommand<LicensePlate>
                ? createNewCar(carCommand.getAggregateId())
                : getExistingCar(carCommand.getAggregateId());

        car.apply(carCommand);
        car.check(domainPolicies);

        processEvents(car.getEvents());

        log.info("Succesfully handled command of type {} for aggregate with id {}", carCommand.getClass(), carCommand.getAggregateId());
    }

    private Car createNewCar(LicensePlate licensePlate) {
        if (eventStore.exists(licensePlate))
            throw new DuplicateException(String.format("Car with license plate %s already exists", licensePlate));

        return new Car(licensePlate);
    }

    private Car getExistingCar(LicensePlate licensePlate) {
        final List<Event<LicensePlate>> eventsForCar = eventStore.getEvents(licensePlate);

        if (eventsForCar.isEmpty())
            throw new NotFoundException(String.format("No car found with license plate %s", licensePlate.value()));

        final var car = new Car(licensePlate);
        car.replay(eventsForCar);

        return car;
    }

    private void processEvents(List<Event<LicensePlate>> events) {
        eventStore.addEvents(events);
        events.forEach(publisher::publishEvent);
    }
}
