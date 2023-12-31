package com.infosupport.repository;

import com.infosupport.entity.CarEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICarEventRepository extends JpaRepository<CarEventEntity, Long> {

    List<CarEventEntity> findAllByLicensePlate(String licensePlate);
}
