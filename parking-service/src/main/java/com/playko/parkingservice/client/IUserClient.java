package com.playko.parkingservice.client;

import com.playko.parkingservice.client.dto.User;
import com.playko.parkingservice.client.interceptor.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "usuarios-service",
        url = "http://localhost:8091/users/v1",
        configuration = FeignClientInterceptor.class)
public interface IUserClient {

    @GetMapping("/admin/user-by-email")
    Optional<User> getUserByEmail(@RequestParam String email);
    @GetMapping("/partner/getUser")
    Optional<User> getUser(@RequestParam String email);
}
