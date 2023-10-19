package com.infosupport.common;

import com.infosupport.car.Car;
import com.infosupport.car.LicensePlate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Slf4j
public abstract class AggregateRoot<I extends AggregateId> {

    private final I id;

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
    }

    public <A extends AggregateRoot<I>> void check(List<DomainPolicy<A, I>> domainPolicies) {
        log.info("Checking domain policies for aggregate with id {}", this.id);
        domainPolicies.forEach(policy ->
                policy.determineFollowUpEvents((A) this).forEach(this::raiseEvent));
    }
}
