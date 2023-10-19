package com.infosupport.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IncomingEvent<I extends AggregateId> {

    @JsonIgnore
    I getAggregateId();
}
