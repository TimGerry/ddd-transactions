package com.infosupport.mapper;

import com.infosupport.entity.CarLicensePlateReadModelEntity;
import com.infosupport.readmodel.CarLicensePlateReadModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarReadModelMapper {

    public static CarLicensePlateReadModel toReadModel(CarLicensePlateReadModelEntity entity) {
        return CarLicensePlateReadModel.builder()
                .licensePlate(entity.getLicensePlate())
                .registeredDateTime(entity.getCreated().toString())
                .build();
    }
}
