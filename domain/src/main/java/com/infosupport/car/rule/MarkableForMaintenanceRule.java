package com.infosupport.car.rule;

import com.infosupport.car.Car;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MarkableForMaintenanceRule {

    public static boolean applies(Car car) {
        return !car.isMaintenanceRequired();
    }
}
