package com.playko.userservice.controller;

import com.playko.userservice.configuration.security.dto.JwtTokenResponseDto;
import com.playko.userservice.configuration.security.dto.LoginRequestDto;
import com.playko.userservice.entities.User;
import com.playko.userservice.service.IAuthHandler;
import com.playko.userservice.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final IUserService userService;
    private final IAuthHandler authHandler;

    @Operation(summary = "Login into the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized for bad credentials", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(this.authHandler.loginUser(loginRequestDto), HttpStatus.OK);
    }
}

