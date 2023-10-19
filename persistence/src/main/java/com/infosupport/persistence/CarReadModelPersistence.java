package com.infosupport.persistence;

import com.infosupport.car.LicensePlate;
import com.infosupport.car.event.CarRegistered;
import com.infosupport.car.event.CarRemoved;
import com.infosupport.common.DomainEvent;
import com.infosupport.entity.CarLicensePlateReadModelEntity;
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
    public void applyToReadModel(DomainEvent<LicensePlate> domainEvent) {
        switch (domainEvent) {
            case CarRegistered carRegistered -> handleCarRegistered(carRegistered);
            case CarRemoved carRemoved -> handleCarRemoved(carRemoved);
            default -> {}/*No operation*/
        }
    }

    private void handleCarRegistered(CarRegistered carRegistered) {
        if (repository.existsByLicensePlate(carRegistered.getLicensePlate())) return;

        final var entity = new CarLicensePlateReadModelEntity(carRegistered.getLicensePlate());
        repository.save(entity);
    }

    private void handleCarRemoved(CarRemoved carRemoved) {
        if (!repository.existsByLicensePlate(carRemoved.getLicensePlate())) return;
        repository.deleteByLicensePlate(carRemoved.getLicensePlate());
    }

    public List<CarLicensePlateReadModel> getAll() {
        return repository.findAll().stream()
                .map(CarReadModelMapper::toReadModel)
                .toList();
    }
}
