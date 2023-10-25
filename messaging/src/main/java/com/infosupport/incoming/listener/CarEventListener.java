package com.infosupport.incoming.listener;

import com.infosupport.incoming.event.CarCrashedEventDto;
import com.infosupport.incoming.mapper.CarEventMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarEventListener {

    private final ApplicationEventPublisher publisher;

//    @KafkaListener(topics = "crash-report", groupId = "${spring.kafka.consumer.group-id}")
//    public void carCrashed(CarCrashedEventDto carCrashedEventDto) {
//        final var carDomainEvent = CarEventMapper.toEvent(carCrashedEventDto);
//        publisher.publishEvent(carDomainEvent);
//    }
}
