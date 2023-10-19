package com.infosupport.controller;


import com.infosupport.car.command.RecordCarTrip;
import com.infosupport.car.command.RegisterCar;
import com.infosupport.dto.CarDrivenDto;
import com.infosupport.dto.CarRegisteredDto;
import com.infosupport.mapper.CarDtoMapper;
import com.infosupport.persistence.CarReadModelPersistence;
import com.infosupport.readmodel.CarLicensePlateReadModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@AllArgsConstructor
@Slf4j
public class CarController {

    private final ApplicationEventPublisher eventPublisher;
    private final CarReadModelPersistence readModelPersistence;

    @GetMapping
    public ResponseEntity<List<CarLicensePlateReadModel>> getCars() {
        return ResponseEntity.ok(readModelPersistence.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerCar(@RequestBody CarRegisteredDto carRegisteredDto) {
        final RegisterCar registerCar = CarDtoMapper.toRegisterCarCommand(carRegisteredDto);

        eventPublisher.publishEvent(registerCar);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/drive")
    public ResponseEntity<Void> driveCar(@RequestBody CarDrivenDto carDrivenDto) {
        final RecordCarTrip recordCarTrip = CarDtoMapper.toRecordCarTrip(carDrivenDto);

        eventPublisher.publishEvent(recordCarTrip);
        return ResponseEntity.ok().build();
    }
}
