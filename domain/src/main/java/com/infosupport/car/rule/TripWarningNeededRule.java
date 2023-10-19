package com.infosupport.car.rule;

import com.infosupport.car.Car;
import com.infosupport.car.CarState;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TripWarningNeededRule {

    public static boolean applies(Car car) {
        return car.getCarState() == CarState.BROKEN;
    }
}
