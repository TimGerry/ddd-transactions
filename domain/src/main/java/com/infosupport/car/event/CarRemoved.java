package com.infosupport.car.event;

import com.infosupport.car.CarState;
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
public class CarRemoved extends DomainEvent<LicensePlate> {

    private String licensePlate;
    private CarState carstate;

    public CarRemoved(String licensePlate, CarState carstate, int version) {
        super(version);
        this.licensePlate = licensePlate;
        this.carstate = carstate;
    }

    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
