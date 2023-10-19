package com.infosupport.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.car.LicensePlate;
import com.infosupport.common.Event;
import com.infosupport.entity.car.CarEventEntity;
import com.infosupport.repository.ICarEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarEventStore {

    private final ObjectMapper objectMapper;
    private final ICarEventRepository repository;

    public void addEvents(List<Event<LicensePlate>> events) {
        final List<CarEventEntity> carEventEntities = events.stream().map(event -> {
            final String eventType = event.getClass().getName();
            final String eventData;
            try {
                eventData = objectMapper.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse event to json", e);
            }

            return new CarEventEntity(event.getAggregateId().value(), eventType, eventData);
        }).toList();

        repository.saveAll(carEventEntities);
    }

    public List<Event<LicensePlate>> getEvents(LicensePlate licensePlate) {
        return repository.findAllByLicensePlate(licensePlate.value())
                .stream()
                .map(this::toCarEvent)
                .toList();
    }

    private Event<LicensePlate> toCarEvent(CarEventEntity entity) {
        try {
            final Class<Event<LicensePlate>> carEventClassType = (Class<Event<LicensePlate>>) Class.forName(entity.getEventType());
            return objectMapper.readValue(entity.getEventData(), carEventClassType);
        } catch (ClassNotFoundException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
