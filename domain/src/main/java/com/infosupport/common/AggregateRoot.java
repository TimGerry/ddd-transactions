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

    private final List<Event<I>> events = new ArrayList<>();

    public abstract void apply(Command<I> command);

    public void replay(List<Event<I>> events) {
        events.forEach(this::handle);
    }

    protected abstract void handle(Event<I> event);

    protected void raiseEvent(Event<I> event) {
        log.info("Raising event of type {} for aggregate with id {}", event.getClass(), this.id);
        handle(event);
        events.add(event);
        version++;
    }
}
