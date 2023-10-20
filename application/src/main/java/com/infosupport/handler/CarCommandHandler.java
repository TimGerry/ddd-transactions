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
import org.springframework.scheduling.annotation.Async;
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

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(Command<LicensePlate> carCommand) throws InterruptedException {
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

//        Thread.sleep(3000);

        log.info("Succesfully handled command of type {} for aggregate with id {}", carCommand.getClass(), carCommand.getAggregateId());
    }

    private void processEvents(Car car) {
        aggregateService.incrementCarAggregateVersion(car.getId(), car.getVersion(), car.getDomainEvents().size());
        eventStore.addEvents(car.getDomainEvents());
        car.getDomainEvents().forEach(publisher::publishEvent);
    }
}
