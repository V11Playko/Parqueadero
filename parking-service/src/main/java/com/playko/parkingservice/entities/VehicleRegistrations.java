package com.playko.parkingservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VehicleRegistrations {

    private String plateNumber;
    private Long registrationCount;
}
