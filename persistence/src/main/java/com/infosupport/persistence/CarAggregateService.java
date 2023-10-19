package com.infosupport.persistence;

import com.infosupport.car.Car;
import com.infosupport.car.LicensePlate;
import com.infosupport.common.Event;
import com.infosupport.entity.car.CarAggregateEntity;
import com.infosupport.exception.ConcurrencyException;
import com.infosupport.exception.DuplicateException;
import com.infosupport.exception.NotFoundException;
import com.infosupport.repository.ICarAggregateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarAggregateService {
    private final ICarAggregateRepository repository;

    public Car createCarAggregate(LicensePlate licensePlate) {
        if (repository.existsByLicensePlate(licensePlate.value()))
            throw new DuplicateException(String.format("Car with license plate %s already exists", licensePlate));

        final CarAggregateEntity savedEntity = repository.save(new CarAggregateEntity(licensePlate.value()));
        return savedEntity.toCar();
    }

    public Car getCarAggregate(LicensePlate licensePlate) {
        final var carAggregateEntity = repository.findByLicensePlate(licensePlate.value())
                .orElseThrow(() -> new NotFoundException(String.format("No car found with license plate %s", licensePlate.value())));

        return carAggregateEntity.toCar();
    }

    public void incrementCarAggregateVersion(LicensePlate licensePlate,int carVersion, int versionIncrease) {
        final var carAggregateEntity = repository.findByLicensePlate(licensePlate.value())
                .orElseThrow(() -> new NotFoundException(String.format("No car found with license plate %s", licensePlate.value())));

        if (carAggregateEntity.getVersion() != carVersion) {
            throw new ConcurrencyException("Aggregate " + licensePlate + " was updated while handling events");
        }

        carAggregateEntity.increaseVersion(versionIncrease);
        repository.save(carAggregateEntity);
    }
}
