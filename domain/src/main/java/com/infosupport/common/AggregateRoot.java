package com.infosupport.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Slf4j
public abstract class AggregateRoot<I extends AggregateId> {

    private final I id;
    private int version;

    private final List<DomainEvent<I>> domainEvents = new ArrayList<>();

    public abstract void apply(Command<I> command);

    public void replay(List<DomainEvent<I>> domainEvents) {
        domainEvents.forEach(this::handle);
    }

    protected abstract void handle(DomainEvent<I> domainEvent);

    protected void raiseEvent(DomainEvent<I> domainEvent) {
        log.info("Raising event of type {} for aggregate with id {}", domainEvent.getClass(), this.id);
        handle(domainEvent);
        domainEvents.add(domainEvent);
        version = domainEvent.getVersion();
    }
}
