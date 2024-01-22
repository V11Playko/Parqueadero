package com.playko.parkingservice.service;

import com.playko.parkingservice.entities.RegistryEntry;

public interface IRegistryEntryService {
    void saveRegistryEntry(RegistryEntry registryEntry, Long parkingId);
}
