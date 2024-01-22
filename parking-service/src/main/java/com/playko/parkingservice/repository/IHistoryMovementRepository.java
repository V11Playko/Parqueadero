package com.playko.parkingservice.repository;

import com.playko.parkingservice.entities.HistoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHistoryMovementRepository extends JpaRepository<HistoryMovement, Long> {

    @Query("SELECT r.plateNumber, COUNT(r) AS registrationCount " +
            "FROM HistoryMovement r " +
            "GROUP BY r.plateNumber " +
            "ORDER BY registrationCount DESC")
    List<Object[]> findTop10RegisteredVehicles();


    @Query("SELECT vehicle.plateNumber, COUNT(vehicle) as registrationCount " +
                  "FROM HistoryMovement vehicle " +
                  "WHERE vehicle.idParking = :id " +
                  "GROUP BY vehicle.plateNumber " +
                  "ORDER BY registrationCount DESC ")
    List<Object[]> findTop10RegisteredVehiclesInParking(@Param("id") Long id);

    @Query("SELECT movement.plateNumber FROM HistoryMovement movement WHERE movement.idParking = :id " +
            "GROUP BY movement.plateNumber HAVING COUNT(movement.plateNumber) = 1")
    List<String> findFirstTimeParkings(@Param("id") Long id);
}
