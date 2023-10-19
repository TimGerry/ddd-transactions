package com.infosupport.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Event<I extends AggregateId> {

    @JsonIgnore
    I getAggregateId();
}
