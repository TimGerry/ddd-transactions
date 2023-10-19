package com.infosupport.car.event;

import com.infosupport.car.LicensePlate;
import com.infosupport.common.DomainEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarRegistered extends DomainEvent<LicensePlate> {

    private String licensePlate;
    private String brand;
    private String model;

    public CarRegistered(String licensePlate, String brand, String model, int version) {
        super(version);
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
    }

    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
