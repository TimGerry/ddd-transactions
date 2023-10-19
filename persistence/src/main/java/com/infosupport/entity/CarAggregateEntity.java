package com.infosupport.entity;

import com.infosupport.car.Car;
import com.infosupport.car.LicensePlate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class CarAggregateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false, updatable = false)
    private String licensePlate;

    @Setter
    @Column(nullable = false)
    private int version;

    public CarAggregateEntity(String licensePlate) {
        this.licensePlate = licensePlate;
        this.version = 0;
    }

    public Car toCar() {
        return new Car(new LicensePlate(licensePlate), version);
    }

    public void increaseVersion(int versionIncrease) {
        version += versionIncrease;
    }
}
