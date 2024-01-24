package com.playko.parkingservice.repository;

import com.playko.parkingservice.entities.ParkingCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IParkingCarsRepository extends JpaRepository<ParkingCars, Long> {

    void deleteByCarPlateAndParkingId(String plate, Long parkingId);
}
