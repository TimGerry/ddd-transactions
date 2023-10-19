package com.infosupport.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DomainEvent<I extends AggregateId> {

    private int version;

    @JsonIgnore
    public abstract I getAggregateId();
}
