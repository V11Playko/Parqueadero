package com.playko.parkingservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Role {
    private Long id;
    private String name;
    private String description;
}

