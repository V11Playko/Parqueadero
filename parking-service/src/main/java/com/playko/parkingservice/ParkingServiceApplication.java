package com.playko.parkingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ParkingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingServiceApplication.class, args);
	}

}
