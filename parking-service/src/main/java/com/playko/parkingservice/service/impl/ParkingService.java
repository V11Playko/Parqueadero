package com.playko.parkingservice.service.impl;

import com.playko.parkingservice.client.IUserClient;
import com.playko.parkingservice.client.dto.User;
import com.playko.parkingservice.entities.HistoryMovement;
import com.playko.parkingservice.entities.Parking;
import com.playko.parkingservice.repository.IHistoryMovementRepository;
import com.playko.parkingservice.repository.IParkingRepository;
import com.playko.parkingservice.service.IParkingService;
import com.playko.parkingservice.service.exceptions.CostPerHourIsRequired;
import com.playko.parkingservice.service.exceptions.InvalidAssignedPartnerException;
import com.playko.parkingservice.service.exceptions.MaximumCapacityIsRequired;
import com.playko.parkingservice.service.exceptions.NameIsRequired;
import com.playko.parkingservice.service.exceptions.ParkingHasNoMovements;
import com.playko.parkingservice.service.exceptions.ParkingNotFoundException;
import com.playko.parkingservice.service.exceptions.UserIsNotPartnerException;
import com.playko.parkingservice.service.exceptions.NoDataFoundException;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ParkingService implements IParkingService {
    private final IParkingRepository parkingRepository;
    private final IUserClient userClient;
    private final IHistoryMovementRepository historyMovementRepository;

    public ParkingService(IParkingRepository parkingRepository, IUserClient userClient, IHistoryMovementRepository historyMovementRepository) {
        this.parkingRepository = parkingRepository;
        this.userClient = userClient;
        this.historyMovementRepository = historyMovementRepository;
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

    /**
     * Recupera las placas de vehículos que han sido registradas por primera vez en un parqueadero.
     *
     * @param id - Identificador único del parqueadero.
     * @return Lista de cadenas que representan las placas de vehículos registradas por primera vez.
     * @throws ParkingNotFoundException - Si no se encuentra el parqueadero con el ID proporcionado.
     * @throws NoDataFoundException - Si no se encuentran datos de vehículos registrados por primera vez en el parqueadero.
     */
    @Override
    public List<String> getFirstTimeParkings(Long id) {
        if (!parkingRepository.existsById(id)) {
            throw new ParkingNotFoundException();
        }
        List<String> firstTimeParkings = historyMovementRepository.findFirstTimeParkings(id);

        if (firstTimeParkings == null || firstTimeParkings.isEmpty()) {
            throw new NoDataFoundException();
        }

        return firstTimeParkings;
    }

    /**
     * Calcula las ganancias de un parqueadero en diferentes períodos: hoy, esta semana, este mes y este año.
     *
     * @param parkingId Identificador único del parqueadero.
     * @return Mapa que contiene las ganancias para cada período.
     * @throws ParkingNotFoundException Si no se encuentra el parqueadero con el ID proporcionado.
     */
    @Override
    public Map<String, Double> getEarningsForPeriod(Long parkingId) {
        Parking parking = parkingRepository.findById(parkingId)
                .orElseThrow(ParkingNotFoundException::new);

        double costPerHour = parseHourlyRate(parking.getCostPerHour());

        List<HistoryMovement> movements = historyMovementRepository.findByIdParking(parkingId);
        if (movements == null || movements.isEmpty()) {
            throw new ParkingHasNoMovements();
        }


        double earningsToday = calculateEarningsForToday(costPerHour, movements);

        double earningsThisWeek = calculateEarningsForThisWeek(costPerHour, movements);

        double earningsThisMonth = calculateEarningsForThisMonth(costPerHour, movements);

        double earningsThisYear = calculateEarningsForThisYear(costPerHour, movements);

        Map<String, Double> earningsMap = new HashMap<>();
        earningsMap.put("today: ", earningsToday);
        earningsMap.put("thisWeek: ", earningsThisWeek);
        earningsMap.put("thisMonth: ", earningsThisMonth);
        earningsMap.put("thisYear: ", earningsThisYear);

        return earningsMap;
    }
    /**  Convierte la tarifa por hora a un valor numérico. */
    private double parseHourlyRate(String costPerHour) {
        String cleanedCostPerHour = costPerHour.replaceAll("[^\\d.]", "");

        return Double.parseDouble(cleanedCostPerHour);
    }

    private double calculateHoursParked(LocalDateTime dateEntry, LocalDateTime dateOut) {
        long minutes = Duration.between(dateEntry, dateOut).toMinutes();
        return  (double) minutes / 60;
    }

    /** Métodos auxiliares para calcular las ganancias para cada período. */
    private double calculateEarningsForToday(double costPerHour, List<HistoryMovement> movements) {
        LocalDate today = LocalDate.now();
        return Math.round(movements.stream()
                .filter(movement -> movement.getDateOut().toLocalDate().isEqual(today))
                .mapToDouble(movement -> {
                    double hoursParked = calculateHoursParked(movement.getDateEntry(), movement.getDateOut());
                    return hoursParked * costPerHour;
                })
                .sum());
    }

    private double calculateEarningsForThisWeek(double costPerHour, List<HistoryMovement> movements) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfTheWeek = today.with(DayOfWeek.MONDAY);
        LocalDate lastDayOfTheWeek = firstDayOfTheWeek.plusDays(6);
        return Math.round(movements.stream()
                .filter(movement -> movement.getDateOut().toLocalDate().isAfter(firstDayOfTheWeek.minusDays(1))
                        && movement.getDateOut().toLocalDate().isBefore(lastDayOfTheWeek.plusDays(1)))
                .mapToDouble(movement -> {
                    double hoursParked = calculateHoursParked(movement.getDateEntry(), movement.getDateOut());
                    return hoursParked * costPerHour;
                })
                .sum());
    }

    private double calculateEarningsForThisMonth(double costPerHour, List<HistoryMovement> movements) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfTheMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfTheMonth = today.withDayOfMonth(today.lengthOfMonth());
        return Math.round(movements.stream()
                .filter(movement -> movement.getDateOut().toLocalDate().isAfter(firstDayOfTheMonth.minusDays(1))
                        && movement.getDateOut().toLocalDate().isBefore(lastDayOfTheMonth.plusDays(1)))
                .mapToDouble(movement -> {
                    double hoursParked = calculateHoursParked(movement.getDateEntry(), movement.getDateOut());
                    return hoursParked * costPerHour;
                })
                .sum());
    }

    private double calculateEarningsForThisYear(double costPerHour, List<HistoryMovement> movements) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfTheYear = today.withDayOfYear(1);
        LocalDate lastDayOfTheYear = today.withDayOfYear(today.lengthOfYear());
        return Math.round(movements.stream()
                .filter(movement -> movement.getDateOut().toLocalDate().isAfter(firstDayOfTheYear.minusDays(1))
                        && movement.getDateOut().toLocalDate().isBefore(lastDayOfTheYear.plusDays(1)))
                .mapToDouble(movement -> {
                    double hoursParked = calculateHoursParked(movement.getDateEntry(), movement.getDateOut());
                    return hoursParked * costPerHour;
                })
                .sum());
    }
}
