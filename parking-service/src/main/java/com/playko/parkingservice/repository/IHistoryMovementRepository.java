package com.playko.parkingservice.repository;

import com.playko.parkingservice.entities.HistoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoryMovementRepository extends JpaRepository<HistoryMovement, Long> {
}
