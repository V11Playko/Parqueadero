package com.playko.parkingservice.service.impl;

import com.playko.parkingservice.client.IMessagingClient;
import com.playko.parkingservice.client.IUserClient;
import com.playko.parkingservice.client.dto.SendNotification;
import com.playko.parkingservice.client.dto.User;
import com.playko.parkingservice.configuration.Constants;
import com.playko.parkingservice.entities.Cars;
import com.playko.parkingservice.entities.Parking;
import com.playko.parkingservice.entities.ParkingCars;
import com.playko.parkingservice.entities.RegistryEntry;
import com.playko.parkingservice.entities.VehicleRegistrations;
import com.playko.parkingservice.repository.ICarsRepository;
import com.playko.parkingservice.repository.IHistoryMovementRepository;
import com.playko.parkingservice.repository.IParkingCarsRepository;
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
    private final ICarsRepository carsRepository;
    private final IParkingCarsRepository parkingCarsRepository;

    public RegistryEntryService(IRegistryEntryRepository registryEntryRepository, IParkingRepository parkingRepository, IUserClient userClient, IMessagingClient messagingClient, IHistoryMovementRepository historyMovementRepository, ICarsRepository carsRepository, IParkingCarsRepository parkingCarsRepository) {
        this.registryEntryRepository = registryEntryRepository;
        this.parkingRepository = parkingRepository;
        this.userClient = userClient;
        this.messagingClient = messagingClient;
        this.historyMovementRepository = historyMovementRepository;
        this.carsRepository = carsRepository;
        this.parkingCarsRepository = parkingCarsRepository;
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
        String plateNumberUpperCase = registryEntry.getPlateNumber().toUpperCase();

        boolean plateExistsInAnyParking =
                registryEntryRepository.existsByPlateNumberAndIdParkingIsNotNull(plateNumberUpperCase);

        if (plateExistsInAnyParking) {
            throw new PlateAlreadyExistsException();
        }

        Parking parking = parkingRepository.findById(parkingId).orElseThrow(ParkingNotFoundException::new);

        Integer maxCapacity = parking.getMaximumCapacity();

        if (registryEntryRepository.countByIdParking(parkingId) >= maxCapacity) {
            throw new ParkingFullException();
        }

        Cars car = new Cars();
        car.setPlate(plateNumberUpperCase);
        carsRepository.save(car);

        ParkingCars parkingCars = new ParkingCars();
        parkingCars.setCar(car);
        parkingCars.setParking(parking);
        parkingCarsRepository.save(parkingCars);

        SendNotification notification = new SendNotification();
        notification.setEmail(parking.getEmailAssignedPartner());
        notification.setPlate(plateNumberUpperCase);
        notification.setMessage(Constants.PLATE_IN_PARKING_MESSAGE);
        notification.setParkingId(parking.getId());

        messagingClient.sendNotification(notification);

        registryEntry.setDateEntry(LocalDateTime.now());
        registryEntry.setIdParking(parkingId);
        registryEntry.setPlateNumber(plateNumberUpperCase);
        registryEntry.setParkingName(parking.getName());
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

        try {
            Optional<User> userclient = userClient.getUser(parking.get().getEmailAssignedPartner());

            if (!parking.get().getEmailAssignedPartner().equals(userclient.get().getEmail())
                    && !parking.get().getEmailAssignedPartner().equals("admin@mail.com")) {
                throw new InvalidAssignedPartnerException();
            }
        } catch (RuntimeException e) {throw new ParkingNotFoundException();}

        if (listVehicle.isEmpty()) {
            throw new NoDataFoundException();
        }

        return listVehicle;
    }

    /**
     * Obtiene la lista de los 10 vehículos más registrados en diferentes parqueaderos, junto con la cantidad de veces que han sido registrados.
     *
     * @return Lista de objetos {@link VehicleRegistrations} que contienen la placa del vehículo y la cantidad de registros.
     * @throws NoDataFoundException - Si no se encuentran datos registrados.
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

    /**
     * Obtiene la lista de hasta 10 vehículos que más veces se han registrado en un parqueadero específico,
     * junto con la cantidad de veces que han sido registrados.
     *
     * @param id - Identificador del parqueadero para el cual se desea obtener la información de los vehículos registrados.
     * @return Lista de hasta 10 objetos {@link VehicleRegistrations}, que contienen información sobre la placa del vehículo
     *         y la cantidad de veces que ha sido registrado en el parqueadero.
     * @throws NoDataFoundException - Se lanza si no se encuentra ningún dato para el parqueadero especificado.
     */
    @Override
    public List<VehicleRegistrations> getTopVehiclesByRegistrationsInParking(Long id) {
        List<Object[]> topVehiclesData = historyMovementRepository.findTop10RegisteredVehiclesInParking(id);

        if (topVehiclesData == null) {
            throw new NoDataFoundException();
        }

        return topVehiclesData.stream()
                .filter(data -> data.length == 2)
                .map(data -> new VehicleRegistrations((String) data[0], (Long) data[1]))
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Busca vehículos estacionados en cualquier parqueadero que coincidan con el patrón de placa proporcionado.
     *
     * @param plateNumber - Patrón de placa a buscar.
     * @return Lista de registros de entrada de vehículos que coinciden con el patrón de placa.
     * @throws NoDataFoundException - Se lanza si no se encuentran vehículos estacionados que coincidan con el patrón.
     */
    @Override
    public List<RegistryEntry> findParkedVehiclesByPlateNumber(String plateNumber) {
        List<RegistryEntry> parkedVehicles =
                registryEntryRepository.findParkedVehiclesByPlateNumber(plateNumber);

        if (parkedVehicles.isEmpty()) {
            throw new NoDataFoundException();
        }

        return parkedVehicles;
    }
}
