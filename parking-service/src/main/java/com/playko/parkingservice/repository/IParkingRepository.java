package com.playko.parkingservice.repository;

import com.playko.parkingservice.entities.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IParkingRepository extends JpaRepository<Parking, Long> {
    List<Parking> findByEmailAssignedPartner(String emailAssignedPartner);
}
