package com.playko.userservice.controller;

import com.playko.userservice.controller.mapper.IUserRequestMapper;
import com.playko.userservice.entities.User;
import com.playko.userservice.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users/v1/partner")
public class PartnerRestController {

    private final IUserService userService;
    private final IUserRequestMapper userRequestMapper;

    public PartnerRestController(IUserService userService, IUserRequestMapper userRequestMapper) {
        this.userService = userService;
        this.userRequestMapper = userRequestMapper;
    }

    @Operation(summary = "Find a user by its email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/get-user")
    public ResponseEntity<User> getUserByEmail(@RequestParam("email") String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
