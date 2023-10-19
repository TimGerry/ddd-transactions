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
public class CarMarkedForMaintenance extends DomainEvent<LicensePlate> {

    private String licensePlate;
    private boolean maintenanceRequired;

    public CarMarkedForMaintenance(String licensePlate, boolean maintenanceRequired, int version) {
        super(version);
        this.licensePlate = licensePlate;
        this.maintenanceRequired = maintenanceRequired;
    }

    @Override
    public LicensePlate getAggregateId() {
        return new LicensePlate(licensePlate);
    }
}
