package com.playko.parkingservice.repository;

import com.playko.parkingservice.entities.RegistryOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegistryOutRepository extends JpaRepository<RegistryOut, Long> {
}
