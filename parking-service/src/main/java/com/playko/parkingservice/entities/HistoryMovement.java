package com.playko.parkingservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "history-movement")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoryMovement implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String plateNumber;

    private LocalDateTime dateEntry;

    private LocalDateTime dateOut;

    private Long idParking;

    private String parkingName;
}
