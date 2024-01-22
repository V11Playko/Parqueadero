package com.playko.messagingservice.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendNotification {
    @NotBlank(message = "El campo 'email' no puede estar en blanco")
    @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida")
    private String email;

    @NotBlank(message = "El campo 'plate' no puede estar en blanco")
    @Size(min = 6, max = 6, message = "El campo 'plate' debe tener exactamente 6 caracteres")
    private String plate;

    @NotBlank(message = "El campo 'message' no puede estar en blanco")
    private String message;

    @NotNull(message = "El campo 'parkingId' no puede ser nulo")
    private Long parkingId;
}
