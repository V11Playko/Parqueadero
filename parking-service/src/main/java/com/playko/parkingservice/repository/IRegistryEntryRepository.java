package com.playko.parkingservice.repository;

import com.playko.parkingservice.entities.RegistryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRegistryEntryRepository extends JpaRepository<RegistryEntry, Long> {
    boolean existsByPlateNumberAndIdParkingIsNotNull(String plateNumber);
    int countByIdParking(Long idParking);

    @Query("SELECT e FROM RegistryEntry e WHERE e.plateNumber = :plateNumber AND e.idParking = :parkingId")
    List<RegistryEntry> findByPlateNumberAndParkingId(@Param("plateNumber") String plateNumber, @Param("parkingId") Long parkingId);
    List<RegistryEntry> findByIdParking(Long parkingId);
}
