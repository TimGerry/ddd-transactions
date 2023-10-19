package com.infosupport.persistence;

import com.infosupport.car.LicensePlate;
import com.infosupport.car.event.CarRegistered;
import com.infosupport.car.event.CarRemoved;
import com.infosupport.common.Event;
import com.infosupport.entity.car.CarLicensePlateReadModelEntity;
import com.infosupport.mapper.CarReadModelMapper;
import com.infosupport.readmodel.CarLicensePlateReadModel;
import com.infosupport.repository.ICarLicensePlateReadModelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CarReadModelPersistence {

    private final ICarLicensePlateReadModelRepository repository;


    @Async
    @EventListener
    @Transactional
    public void applyToReadModel(Event<LicensePlate> event) {
        switch (event) {
            case CarRegistered carRegistered -> handleCarRegistered(carRegistered);
            case CarRemoved carRemoved -> handleCarRemoved(carRemoved);
            default -> {}/*No operation*/
        }
    }

    private void handleCarRegistered(CarRegistered carRegistered) {
        if (repository.existsByLicensePlate(carRegistered.licensePlate())) return;

        final var entity = new CarLicensePlateReadModelEntity(carRegistered.licensePlate());
        repository.save(entity);
    }

    private void handleCarRemoved(CarRemoved carRemoved) {
        if (!repository.existsByLicensePlate(carRemoved.licensePlate())) return;
        repository.deleteByLicensePlate(carRemoved.licensePlate());
    }

    public List<CarLicensePlateReadModel> getAll() {
        return repository.findAll().stream()
                .map(CarReadModelMapper::toReadModel)
                .toList();
    }
}
