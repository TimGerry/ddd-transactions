package com.infosupport.entity.car;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor()
public class CarLicensePlateReadModelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false, updatable = false)
    private String licensePlate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant created;

    public CarLicensePlateReadModelEntity(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
