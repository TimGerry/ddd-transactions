package com.infosupport.incoming.mapper;

import com.infosupport.car.event.CarCrashed;
import com.infosupport.incoming.event.CarCrashedEventDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarEventMapper {

    public static CarCrashed toDomainEvent(CarCrashedEventDto carCrashedEventDto) {
        return new CarCrashed(carCrashedEventDto.licensePlate(), carCrashedEventDto.carState());
    }
}
