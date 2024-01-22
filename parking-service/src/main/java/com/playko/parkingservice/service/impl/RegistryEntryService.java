package com.playko.parkingservice.service.impl;

import com.playko.parkingservice.entities.Parking;
import com.playko.parkingservice.entities.RegistryEntry;
import com.playko.parkingservice.repository.IParkingRepository;
import com.playko.parkingservice.repository.IRegistryEntryRepository;
import com.playko.parkingservice.service.IRegistryEntryService;
import com.playko.parkingservice.service.exceptions.NoDataFoundException;
import com.playko.parkingservice.service.exceptions.ParkingFullException;
import com.playko.parkingservice.service.exceptions.ParkingNotFoundException;
import com.playko.parkingservice.service.exceptions.PlateAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class RegistryEntryService implements IRegistryEntryService {
    private final IRegistryEntryRepository registryEntryRepository;
    private final IParkingRepository parkingRepository;

    public RegistryEntryService(IRegistryEntryRepository registryEntryRepository, IParkingRepository parkingRepository) {
        this.registryEntryRepository = registryEntryRepository;
        this.parkingRepository = parkingRepository;
    }


    /**
     * Registra la entrada de un vehículo en un parqueadero.
     *
     * @param registryEntry - Información del registro de entrada.
     * @param parkingId - Identificador del parqueadero.
     * @throws PlateAlreadyExistsException - Se lanza si ya existe un vehículo con la misma placa en algún parqueadero.
     * @throws ParkingNotFoundException - Se lanza si el parqueadero con el ID proporcionado no existe.
     * @throws ParkingFullException - Se lanza si el parqueadero está lleno y no puede aceptar más vehículos.
     */
    @Override
    public void saveRegistryEntry(RegistryEntry registryEntry, Long parkingId) {
        boolean plateExistsInAnyParking =
                registryEntryRepository.existsByPlateNumberAndIdParkingIsNotNull(registryEntry.getPlateNumber());

        if (plateExistsInAnyParking) {
            throw new PlateAlreadyExistsException();
        }

        Parking parking = parkingRepository.findById(parkingId).orElseThrow(ParkingNotFoundException::new);

        Integer maxCapacity = parking.getMaximumCapacity();

        if (registryEntryRepository.countByIdParking(parkingId) >= maxCapacity) {
            throw new ParkingFullException();
        }

        registryEntry.setDateEntry(LocalDateTime.now());
        registryEntry.setIdParking(parkingId);

        registryEntryRepository.save(registryEntry);
    }
}
