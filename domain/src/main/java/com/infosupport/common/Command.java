package com.infosupport.common;

public interface Command<I extends AggregateId> {

    I getAggregateId();
}
