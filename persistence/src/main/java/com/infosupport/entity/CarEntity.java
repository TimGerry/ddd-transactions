package com.infosupport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

// TODO: store version property & propagate to readmodel with created and updated properties
// TODO: add aggregate entity
@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CarEntity {

    @Id
    @Column(name = "license_plate", nullable = false, updatable = false)
    private String licensePlate;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @CreationTimestamp
    @Column(name = "created", nullable = false, updatable = false)
    private Instant created;

    @UpdateTimestamp
    private Instant updated;
}
