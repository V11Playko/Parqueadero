package com.playko.parkingservice.service.impl;

import com.playko.parkingservice.client.IUserClient;
import com.playko.parkingservice.client.dto.User;
import com.playko.parkingservice.entities.Parking;
import com.playko.parkingservice.repository.IParkingRepository;
import com.playko.parkingservice.service.IParkingService;
import com.playko.parkingservice.service.exceptions.CostPerHourIsRequired;
import com.playko.parkingservice.service.exceptions.InvalidAssignedPartnerException;
import com.playko.parkingservice.service.exceptions.MaximumCapacityIsRequired;
import com.playko.parkingservice.service.exceptions.NameIsRequired;
import com.playko.parkingservice.service.exceptions.UserIsNotPartnerException;
import com.playko.parkingservice.service.exceptions.NoDataFoundException;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParkingService implements IParkingService {
    private final IParkingRepository parkingRepository;
    private final IUserClient userClient;

    public ParkingService(IParkingRepository parkingRepository, IUserClient userClient) {
        this.parkingRepository = parkingRepository;
        this.userClient = userClient;
    }


    /**
     * Guardar un parqueadero
     * Guarda un nuevo parqueadero en el sistema.
     *
     * @param parking - Informacion del parqueadero
     * @throws UserIsNotPartnerException - Se lanza si el usuario asociado no es un socio
     * @throws NoDataFoundException - Se lanza si no se encuentra el usuario asociado
     * */
    @Override
    public void saveParking(Parking parking) {
        try {
            Optional<User> user = userClient.getUserByEmail(parking.getEmailAssignedPartner());

            if (!user.get().getRole().getName().equals("ROLE_PARTNER")) {
                throw new UserIsNotPartnerException();
            }

            String costPerHourFormatted = "$" + parking.getCostPerHour();
            parking.setCostPerHour(costPerHourFormatted);

            parkingRepository.save(parking);
        } catch (FeignException.NotFound ex) {
            throw new NoDataFoundException();
        }
    }


    /**
     * Obtiene la información de un parqueadero por su identificador único.
     *
     * @param id - Identificador único del parqueadero
     * @return Un objeto Optional que contiene la información del parqueadero si se encuentra, de lo contrario, lanza una NoDataFoundException
     * @throws NoDataFoundException - Se lanza si no se encuentra el parqueadero con el ID especificado
     */
    @Override
    public Optional<Parking> getParking(Long id) {
        return Optional.ofNullable(parkingRepository.findById(id).orElseThrow(NoDataFoundException::new));
    }


    /**
     * Actualiza la información de un parqueadero.
     *
     * @param parking - Información actualizada del parqueadero
     * @param emailAssignedPartner - Correo electrónico del socio asignado al parqueadero
     * @throws NoDataFoundException - Se lanza si no se encuentra el parqueadero con el ID especificado
     * @throws InvalidAssignedPartnerException - Se lanza si el socio asignado al parqueadero no coincide con el proporcionado
     * @throws NameIsRequired - Se lanza si el nuevo nombre del parqueadero es nulo o vacío
     * @throws MaximumCapacityIsRequired - Se lanza si la nueva capacidad máxima del parqueadero es nula
     * @throws CostPerHourIsRequired - Se lanza si el nuevo costo por hora del parqueadero es nulo o vacío
     */
    @Override
    public void updateParking(Parking parking, String emailAssignedPartner) {
        Optional<Parking> existingParking =
                Optional.ofNullable(getParking(parking.getId()).orElseThrow(NoDataFoundException::new));

        if (!existingParking.get().getEmailAssignedPartner().equals(emailAssignedPartner)) {
            throw new InvalidAssignedPartnerException();
        }

        if (parking.getName() != null && !parking.getName().isEmpty()) {
            existingParking.get().setName(parking.getName());
        } else {
            throw new NameIsRequired();
        }

        if (parking.getMaximumCapacity() != null) {
            existingParking.get().setMaximumCapacity(parking.getMaximumCapacity());
        } else {
            throw new MaximumCapacityIsRequired();
        }

        if (parking.getCostPerHour() != null && !parking.getCostPerHour().isEmpty()) {
            existingParking.get().setCostPerHour("$" + parking.getCostPerHour());
        } else {
            throw new CostPerHourIsRequired();
        }

        String costPerHourFormatted = "$" + parking.getCostPerHour();
        parking.setCostPerHour(costPerHourFormatted);

        parkingRepository.save(existingParking.get());
    }

    /**
     * Elimina un parqueadero por su identificador único.
     *
     * @param id - Identificador único del parqueadero a eliminar
     * @throws NoDataFoundException - Se lanza si no se encuentra el parqueadero con el ID especificado
     */
    @Override
    public void deleteParking(Long id) {
        getParking(id).orElseThrow(NoDataFoundException::new);

        parkingRepository.deleteById(id);
    }

    /**
     * Obtiene la lista de parqueaderos asociados a un socio.
     *
     * @param emailAssignedPartner El correo electrónico del socio.
     * @return Lista de parqueaderos asociados al socio.
     * @throws UserIsNotPartnerException - Si el usuario no tiene el rol de socio.
     * @throws NoDataFoundException - Si no se encuentra el usuario asociado al correo electrónico.
     */
    @Override
    public List<Parking> getAssociatedParkings(String emailAssignedPartner) {
        Optional<User> user = Optional.ofNullable
                (userClient.getUser(emailAssignedPartner).orElseThrow(NoDataFoundException::new));

        if (user.isPresent() && user.get().getRole().getName().equals("ROLE_PARTNER")) {
            return parkingRepository.findByEmailAssignedPartner(emailAssignedPartner);
        } else throw new UserIsNotPartnerException();
    }
}
