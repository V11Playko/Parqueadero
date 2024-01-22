package com.playko.parkingservice.service;

import com.playko.parkingservice.entities.Parking;
import com.playko.parkingservice.entities.RegistryEntry;

import java.util.List;

public interface IRegistryEntryService {
    void saveRegistryEntry(RegistryEntry registryEntry, Long parkingId);
    List<RegistryEntry> getListSpecificParkingVehicles(Long parkingId);
    List<RegistryEntry> getListVehicles(Long parkingId);
}
