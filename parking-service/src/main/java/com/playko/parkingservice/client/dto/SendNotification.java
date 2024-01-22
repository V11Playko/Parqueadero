package com.playko.parkingservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendNotification {
    private String email;
    private String plate;
    private String message;
    private Long parkingId;
}
