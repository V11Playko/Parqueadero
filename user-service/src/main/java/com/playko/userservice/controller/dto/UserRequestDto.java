package com.playko.userservice.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserRequestDto {

    @NotBlank(message = "El campo 'name' es obligatorio")
    private String name;

    @NotBlank(message = "El campo 'surname' es obligatorio")
    private String surname;

    @Pattern(regexp = "^\\+?57\\s(3[0-2]|7[0-1])\\d{8}$", message = "El campo 'phoneNumber' debe ser un número de teléfono válido. Introduzca el formato +57 3...")
    @NotBlank(message = "El campo 'phoneNumber' es obligatorio")
    private String phoneNumber;

    @NotBlank(message = "El campo 'email' es obligatorio")
    @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida. Introduzca el formato nombre@ejemplo.com")
    private String email;

    @NotBlank(message = "El campo 'password' es obligatorio")
    private String password;

    private Long role;
}
