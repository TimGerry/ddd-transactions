package com.infosupport.common;

import com.infosupport.car.Car;
import com.infosupport.car.LicensePlate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class AggregateRoot<I extends AggregateId> {

    private final I id;

    private final List<Event<I>> events = new ArrayList<>();

    public abstract void apply(Command<I> command);

    public void replay(List<Event<I>> events) {
        events.forEach(this::handle);
    }

    protected abstract void handle(Event<I> event);

    protected void raiseEvent(Event<I> event) {
        handle(event);
        events.add(event);
    }

    public <A extends AggregateRoot<I>> void check(List<DomainPolicy<A, I>> domainPolicies) {
        domainPolicies.forEach(policy ->
                policy.determineFollowUpEvents((A) this).forEach(this::raiseEvent));
    }
}
