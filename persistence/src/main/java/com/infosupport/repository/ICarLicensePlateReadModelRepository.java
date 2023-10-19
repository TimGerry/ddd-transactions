package com.infosupport.repository;

import com.infosupport.entity.car.CarLicensePlateReadModelEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface ICarLicensePlateReadModelRepository extends ListCrudRepository<CarLicensePlateReadModelEntity, Long> {
    boolean existsByLicensePlate(String licensePlate);
    void deleteByLicensePlate(String licensePlate);
}
