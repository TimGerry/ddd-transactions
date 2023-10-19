package com.infosupport.mapper;

import com.infosupport.car.command.RecordCarTrip;
import com.infosupport.car.command.RegisterCar;
import com.infosupport.dto.CarDrivenDto;
import com.infosupport.dto.CarRegisteredDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarDtoMapper {

    public static RegisterCar toRegisterCarCommand(CarRegisteredDto carRegisteredDto) {
        return new RegisterCar(
                carRegisteredDto.licensePlate(),
                carRegisteredDto.brand(),
                carRegisteredDto.model()
        );
    }

    public static RecordCarTrip toRecordCarTrip(CarDrivenDto carDrivenDto) {
        return new RecordCarTrip(
                carDrivenDto.licensePlate(),
                carDrivenDto.tripDistanceKm(),
                carDrivenDto.carState()
        );
    }
}
