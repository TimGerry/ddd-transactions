package com.infosupport.readmodel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CarReadModel {
    private String licensePlate;
    private String brand;
    private String model;
    private String registeredDateTime;
}
