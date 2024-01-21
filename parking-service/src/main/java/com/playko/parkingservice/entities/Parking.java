package com.playko.parkingservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "parking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Parking implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank(message = "El campo 'name' es obligatorio")
    private String name;

    @NotNull(message = "El campo 'maximumCapacity' no puede ser nulo")
    private Integer maximumCapacity;

    @NotBlank(message = "El campo 'costPerHour' es obligatorio")
    private String costPerHour;

    @NotBlank(message = "El campo 'emailAssignedPartner' es obligatorio")
    private String emailAssignedPartner;
}
