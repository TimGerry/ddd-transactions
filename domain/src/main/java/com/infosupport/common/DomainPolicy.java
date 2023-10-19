package com.infosupport.common;

import java.util.List;

public interface DomainPolicy<A extends AggregateRoot<I>, I extends AggregateId> {

    List<Event<I>> determineFollowUpEvents(A aggregate);
}
