package com.infosupport.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.car.LicensePlate;
import com.infosupport.common.DomainEvent;
import com.infosupport.entity.CarEventEntity;
import com.infosupport.repository.ICarEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarEventStore {

    private final ObjectMapper objectMapper;
    private final ICarEventRepository repository;

    public void addEvents(List<DomainEvent<LicensePlate>> domainEvents) {
        final List<CarEventEntity> carEventEntities = domainEvents.stream().map(event -> {
            final String eventType = event.getClass().getName();
            final String eventData;
            try {
                eventData = objectMapper.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse event to json", e);
            }

            return new CarEventEntity(event.getAggregateId().value(), eventType, eventData, event.getVersion());
        }).toList();

        repository.saveAll(carEventEntities);
    }

    public List<DomainEvent<LicensePlate>> getEvents(LicensePlate licensePlate) {
        return repository.findAllByLicensePlate(licensePlate.value())
                .stream()
                .map(this::toCarEvent)
                .toList();
    }

    private DomainEvent<LicensePlate> toCarEvent(CarEventEntity entity) {
        try {
            final Class<DomainEvent<LicensePlate>> carEventClassType = (Class<DomainEvent<LicensePlate>>) Class.forName(entity.getEventType());
            return objectMapper.readValue(entity.getEventData(), carEventClassType);
        } catch (ClassNotFoundException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
