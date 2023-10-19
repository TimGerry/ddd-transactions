package com.infosupport.repository;

import com.infosupport.entity.CarAggregateEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ICarAggregateRepository extends ListCrudRepository<CarAggregateEntity, Long> {
    boolean existsByLicensePlate(String licensePlate);
    Optional<CarAggregateEntity> findByLicensePlate(String licensePlate);
}
