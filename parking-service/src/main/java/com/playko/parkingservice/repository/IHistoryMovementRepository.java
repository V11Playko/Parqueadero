package com.playko.parkingservice.repository;

import com.playko.parkingservice.entities.HistoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHistoryMovementRepository extends JpaRepository<HistoryMovement, Long> {

    @Query("SELECT r.plateNumber, COUNT(r) AS registrationCount " +
            "FROM HistoryMovement r " +
            "GROUP BY r.plateNumber " +
            "ORDER BY registrationCount DESC")
    List<Object[]> findTop10RegisteredVehicles();
}
