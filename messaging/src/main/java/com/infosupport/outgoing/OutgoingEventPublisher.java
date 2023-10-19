package com.infosupport.outgoing;

import com.infosupport.common.AggregateId;
import com.infosupport.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OutgoingEventPublisher {

    private final KafkaTemplate<String, DomainEvent<? extends AggregateId>> kafkaTemplate;

    @EventListener
    public void publish(DomainEvent<? extends AggregateId> domainEvent) {
        kafkaTemplate.send("car", domainEvent);
        log.info("Outgoing event of type {} is on its way...", domainEvent.getClass().getName());
    }
}
