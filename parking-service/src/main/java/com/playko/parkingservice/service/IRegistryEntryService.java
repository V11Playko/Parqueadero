package com.playko.parkingservice.service;

import com.playko.parkingservice.entities.RegistryEntry;
import com.playko.parkingservice.entities.VehicleRegistrations;

import java.util.List;

public interface IRegistryEntryService {
    void saveRegistryEntry(RegistryEntry registryEntry, Long parkingId);
    List<RegistryEntry> getListSpecificParkingVehicles(Long parkingId);
    List<RegistryEntry> getListVehicles(Long parkingId);
    List<VehicleRegistrations>getTopVehiclesByRegistrations();
    List<VehicleRegistrations> getTopVehiclesByRegistrationsInParking(Long id);
    List<RegistryEntry> findParkedVehiclesByPlateNumber(String plateNumber);
}
