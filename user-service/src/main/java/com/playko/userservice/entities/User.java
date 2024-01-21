package com.playko.userservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank(message = "El campo 'name' es obligatorio")
    @Pattern(regexp = "^[^0-9]+$", message = "El campo 'name' no debe contener números")
    private String name;

    @NotBlank(message = "El campo 'surname' es obligatorio")
    @Pattern(regexp = "^[^0-9]+$", message = "El campo 'surname' no debe contener números")
    private String surname;

    @Pattern(regexp = "^\\+?57\\s(3[0-2]|7[0-1])\\d{8}$", message = "El campo 'phoneNumber' debe ser un número de teléfono válido. Introduzca el formato +57 3...")
    @NotBlank(message = "El campo 'phoneNumber' es obligatorio")
    private String phoneNumber;

    @NotBlank(message = "El campo 'email' es obligatorio")
    @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida. Introduzca el formato nombre@ejemplo.com")
    private String email;

    @NotBlank(message = "El campo 'password' es obligatorio")
    private String password;

    @NotNull(message = "El campo 'rol' es obligatorio")
    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;
}
