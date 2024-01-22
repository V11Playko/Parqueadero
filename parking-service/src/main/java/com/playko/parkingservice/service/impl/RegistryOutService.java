package com.playko.parkingservice.service.impl;

import com.playko.parkingservice.entities.HistoryMovement;
import com.playko.parkingservice.entities.RegistryEntry;
import com.playko.parkingservice.entities.RegistryOut;
import com.playko.parkingservice.repository.IHistoryMovementRepository;
import com.playko.parkingservice.repository.IRegistryEntryRepository;
import com.playko.parkingservice.repository.IRegistryOutRepository;
import com.playko.parkingservice.service.IRegistryOutService;
import com.playko.parkingservice.service.exceptions.PlateNotRegisteredException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RegistryOutService implements IRegistryOutService {
    private final IRegistryEntryRepository registryEntryRepository;
    private final IRegistryOutRepository registryOutRepository;
    private final IHistoryMovementRepository historyMovementRepository;

    public RegistryOutService(IRegistryEntryRepository registryEntryRepository, IRegistryOutRepository registryOutRepository, IHistoryMovementRepository historyMovementRepository) {
        this.registryEntryRepository = registryEntryRepository;
        this.registryOutRepository = registryOutRepository;
        this.historyMovementRepository = historyMovementRepository;
    }

    /**
     * Registra la salida de un vehículo del parqueadero.
     *
     * Este método se utiliza para registrar la salida de un vehículo del parqueadero. Busca los registros de entrada
     * correspondientes a la placa y el parqueadero especificados, y crea un historial del movimiento. Además, elimina el
     * registro de entrada asociado y guarda la información de salida en la base de datos.
     *
     * @param registryOut - Información del registro de salida.
     * @throws PlateNotRegisteredException - Se lanza si no hay ningún registro de entrada para la placa y el parqueadero especificados.
     */
    @Override
    public void saveRegistryOut(RegistryOut registryOut) {
        String plateNumber = registryOut.getPlateNumber();
        Long parkingId = registryOut.getIdParking();

        List<RegistryEntry> matchingEntries =
                registryEntryRepository.findByPlateNumberAndParkingId(plateNumber, parkingId);

        if (matchingEntries.isEmpty()) {
            throw new PlateNotRegisteredException();
        }

        HistoryMovement historyMovement = new HistoryMovement();
        historyMovement.setPlateNumber(plateNumber);
        historyMovement.setDateEntry(matchingEntries.get(0).getDateEntry());
        historyMovement.setDateOut(LocalDateTime.now());
        historyMovement.setIdParking(parkingId);

        historyMovementRepository.save(historyMovement);

        registryEntryRepository.delete(matchingEntries.get(0));

        registryOut.setDateOut(LocalDateTime.now());
        registryOutRepository.save(registryOut);
    }
}
