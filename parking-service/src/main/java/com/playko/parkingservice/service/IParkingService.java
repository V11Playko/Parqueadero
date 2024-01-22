package com.playko.parkingservice.service;

import com.playko.parkingservice.entities.HistoryMovement;
import com.playko.parkingservice.entities.Parking;

import java.util.List;
import java.util.Optional;

public interface IParkingService {
    void saveParking (Parking parking);
    Optional<Parking> getParking(Long id);
    void updateParking(Parking parking, String emailAssignedPartner);
    void deleteParking(Long id);
    List<Parking> getAssociatedParkings(String emailAssignedPartner);
    List<String> getFirstTimeParkings(Long id);
}
