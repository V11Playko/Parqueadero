package com.playko.parkingservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "registry-out")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistryOut implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "La placa debe tener 6 caracteres alfanuméricos.")
    @Size(min = 6, max = 6, message = "La placa debe tener 6 caracteres de longitud.")
    private String plateNumber;

    private LocalDateTime dateOut;

    private Long idParking;

    private String parkingName;
}
