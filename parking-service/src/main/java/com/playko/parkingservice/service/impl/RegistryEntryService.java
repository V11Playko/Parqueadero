package com.playko.parkingservice.service.impl;

import com.playko.parkingservice.client.IMessagingClient;
import com.playko.parkingservice.client.IUserClient;
import com.playko.parkingservice.client.dto.SendNotification;
import com.playko.parkingservice.client.dto.User;
import com.playko.parkingservice.configuration.Constants;
import com.playko.parkingservice.entities.Parking;
import com.playko.parkingservice.entities.RegistryEntry;
import com.playko.parkingservice.entities.VehicleRegistrations;
import com.playko.parkingservice.repository.IHistoryMovementRepository;
import com.playko.parkingservice.repository.IParkingRepository;
import com.playko.parkingservice.repository.IRegistryEntryRepository;
import com.playko.parkingservice.service.IRegistryEntryService;
import com.playko.parkingservice.service.exceptions.InvalidAssignedPartnerException;
import com.playko.parkingservice.service.exceptions.NoDataFoundException;
import com.playko.parkingservice.service.exceptions.ParkingFullException;
import com.playko.parkingservice.service.exceptions.ParkingNotFoundException;
import com.playko.parkingservice.service.exceptions.PlateAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RegistryEntryService implements IRegistryEntryService {
    private final IRegistryEntryRepository registryEntryRepository;
    private final IParkingRepository parkingRepository;
    private final IUserClient userClient;
    private final IMessagingClient messagingClient;
    private final IHistoryMovementRepository historyMovementRepository;

    public RegistryEntryService(IRegistryEntryRepository registryEntryRepository, IParkingRepository parkingRepository, IUserClient userClient, IMessagingClient messagingClient, IHistoryMovementRepository historyMovementRepository) {
        this.registryEntryRepository = registryEntryRepository;
        this.parkingRepository = parkingRepository;
        this.userClient = userClient;
        this.messagingClient = messagingClient;
        this.historyMovementRepository = historyMovementRepository;
    }


    /**
     * Registra la entrada de un vehículo en un parqueadero
     * y se le envia una notificacion al servicio de mensajeria
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

        SendNotification notification = new SendNotification();
        notification.setEmail(parking.getEmailAssignedPartner());
        notification.setPlate(registryEntry.getPlateNumber());
        notification.setMessage(Constants.PLATE_IN_PARKING_MESSAGE);
        notification.setParkingId(parking.getId());

        messagingClient.sendNotification(notification);

        registryEntry.setDateEntry(LocalDateTime.now());
        registryEntry.setIdParking(parkingId);

        registryEntryRepository.save(registryEntry);
    }

    /**
     * Obtiene la lista de vehículos registrados en un parqueadero en especifico.
     *
     * @param parkingId - El ID del parqueadero del cual se desea obtener la lista de vehículos.
     * @return Lista de registros de entrada correspondientes al parqueadero especificado.
     * @throws NoDataFoundException - Se lanza si no se encuentran datos para el parqueadero especificado.
     */
    @Override
    public List<RegistryEntry> getListSpecificParkingVehicles(Long parkingId) {
        List<RegistryEntry> listVehicle = registryEntryRepository.findByIdParking(parkingId);

        if (listVehicle.isEmpty()) {
            throw new NoDataFoundException();
        }

        return listVehicle;
    }

    /**
     * Obtiene la lista de vehículos registrados en un parqueadero que le pertenezca al socio.
     *
     * @param parkingId - El ID del parqueadero del cual se desea obtener la lista de vehículos.
     * @return Lista de registros de entrada correspondientes al parqueadero especificado.
     * @throws InvalidAssignedPartnerException - Se lanza si el socio asignado al parqueadero no coincide con el proporcionado
     * @throws NoDataFoundException - Se lanza si no se encuentran datos para el parqueadero especificado.
     */
    @Override
    public List<RegistryEntry> getListVehicles(Long parkingId) {
        Optional<Parking> parking = parkingRepository.findById(parkingId);
        List<RegistryEntry> listVehicle = registryEntryRepository.findByIdParking(parkingId);

        Optional<User> userclient = userClient.getUser(parking.get().getEmailAssignedPartner());

        if (!parking.get().getEmailAssignedPartner().equals(userclient.get().getEmail())
                && !parking.get().getEmailAssignedPartner().equals("admin@mail.com")) {
            throw new InvalidAssignedPartnerException();
        }
        if (listVehicle.isEmpty()) {
            throw new NoDataFoundException();
        }

        return listVehicle;
    }

    /**
     * Obtiene la lista de los 10 vehículos más registrados en diferentes parqueaderos, junto con la cantidad de veces que han sido registrados.
     *
     * @return Lista de objetos {@link VehicleRegistrations} que contienen la placa del vehículo y la cantidad de registros.
     * @throws NoDataFoundException Si no se encuentran datos registrados.
     */
    @Override
    public List<VehicleRegistrations> getTopVehiclesByRegistrations() {
        List<Object[]> topVehiclesData = historyMovementRepository.findTop10RegisteredVehicles();

        if (topVehiclesData == null) {
            throw new NoDataFoundException();
        }

        return topVehiclesData.stream()
                .filter(data -> data.length == 2)
                .map(data -> new VehicleRegistrations((String) data[0], (Long) data[1]))
                .limit(10)
                .collect(Collectors.toList());
    }
}
