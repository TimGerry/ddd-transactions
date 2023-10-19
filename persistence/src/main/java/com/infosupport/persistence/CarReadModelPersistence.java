package com.infosupport.persistence;

import com.infosupport.car.LicensePlate;
import com.infosupport.car.event.CarMarkedForMaintenance;
import com.infosupport.car.event.CarRegistered;
import com.infosupport.car.event.CarRemoved;
import com.infosupport.car.event.CarTripRecorded;
import com.infosupport.common.Event;
import com.infosupport.entity.CarEntity;
import com.infosupport.mapper.CarReadModelMapper;
import com.infosupport.readmodel.CarReadModel;
import com.infosupport.repository.ICarReadModelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CarReadModelPersistence {

    private final ICarReadModelRepository repository;


//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @Async
//    @Transactional
    public void applyToReadModel(Event<LicensePlate> event) {
        switch (event) {
            case CarRegistered carRegistered -> handleCarRegistered(carRegistered);
            case CarRemoved carRemoved -> handleCarRemoved(carRemoved);
            default -> {}/*No operation*/
        }
    }

    private void handleCarRegistered(CarRegistered carRegistered) {
        final var carEntity = CarEntity.builder()
                .licensePlate(carRegistered.licensePlate())
                .brand(carRegistered.brand())
                .model(carRegistered.licensePlate())
                .build();
        repository.save(carEntity);
        log.info("Car count: " + repository.count());
    }

    private void handleCarRemoved(CarRemoved carRemoved) {
        repository.deleteById(carRemoved.licensePlate());
    }

    public List<CarReadModel> getAll() {
        return repository.findAll().stream()
                .map(CarReadModelMapper::toReadModel)
                .toList();
    }
}
