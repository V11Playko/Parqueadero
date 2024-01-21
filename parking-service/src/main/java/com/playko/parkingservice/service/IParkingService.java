package com.playko.parkingservice.service;

import com.playko.parkingservice.entities.Parking;

import java.util.Optional;

public interface IParkingService {
    void saveParking (Parking parking);
    Optional<Parking> getParking(Long id);
    void updateParking(Parking parking, String emailAssignedPartner);
    void deleteParking(Long id);
}
