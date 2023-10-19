package com.infosupport.mapper;

import com.infosupport.car.Car;
import com.infosupport.entity.CarEntity;
import com.infosupport.readmodel.CarReadModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarReadModelMapper {

    public static CarReadModel toReadModel(CarEntity entity) {
        return CarReadModel.builder()
                .licensePlate(entity.getLicensePlate())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .registeredDateTime(entity.getCreated().toString())
                .build();
    }
}
