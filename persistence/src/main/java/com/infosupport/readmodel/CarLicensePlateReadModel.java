package com.infosupport.readmodel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CarLicensePlateReadModel {
    private String licensePlate;
    private String registeredDateTime;
}
