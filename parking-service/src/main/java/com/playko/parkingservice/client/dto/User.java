package com.playko.parkingservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String password;
    private Role role;
}
