package com.playko.parkingservice.repository;

import com.playko.parkingservice.entities.Cars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICarsRepository extends JpaRepository<Cars, Long> {
}
