package com.infosupport.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
public class CarEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int version;

    @Column(name = "license_plate", nullable = false, updatable = false)
    private String licensePlate;

    @Column(name = "event_type", nullable = false, updatable = false)
    private String eventType;

    @Column(name = "event_data", nullable = false, updatable = false)
    private String eventData;

    @CreationTimestamp
    @Column(name = "created", nullable = false, updatable = false)
    private Instant created;

    public CarEventEntity(String licensePlate, String eventType, String eventData, int version) {
        this.version = version;
        this.licensePlate = licensePlate;
        this.eventType = eventType;
        this.eventData = eventData;
    }
}
