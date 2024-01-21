package com.playko.parkingservice.controller;

import com.playko.parkingservice.configuration.Constants;
import com.playko.parkingservice.entities.Parking;
import com.playko.parkingservice.service.IParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin/v1")
public class AdminController {
    private final IParkingService parkingService;

    public AdminController(IParkingService parkingService) {
        this.parkingService = parkingService;
    }


    @Operation(summary = "Add a new parking",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Parking created",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Parking already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),})
    @PostMapping("/save-parking")
    public ResponseEntity<Map<String, String>> saveParking(@Valid @RequestBody Parking parking) {
        parkingService.saveParking(parking);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.PARKING_CREATED_MESSAGE));
    }


    @Operation(summary = "Get parking",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The parking returned",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Parking.class)))),
                    @ApiResponse(responseCode = "404", description = "No data found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @GetMapping("/parking/{id}")
    public ResponseEntity<Optional<Parking>> getParking(@PathVariable Long id) {
        return ResponseEntity.ok(parkingService.getParking(id));
    }


    @Operation(summary = "Update parking",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The parking update",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Parking.class)))),
                    @ApiResponse(responseCode = "404", description = "No data found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PutMapping("/updateParking/{id}")
    public ResponseEntity<Map<String, String>> updateParking(@PathVariable("id") Long id,
                                                          @RequestBody Parking parking,
                                                          @RequestParam("emailAssignedPartner") String emailAssignedPartner) {
        parking.setId(id);
        parkingService.updateParking(parking, emailAssignedPartner);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.PARKING_UPDATE_MESSAGE));

    }


    @Operation(summary = "Delete parking by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The parking delete",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Parking.class)))),
                    @ApiResponse(responseCode = "404", description = "No data found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @DeleteMapping("/deleteParking/{id}")
    public ResponseEntity<Map<String, String>> deleteParkingById(@PathVariable Long id) {
        parkingService.deleteParking(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.PARKING_DELETE_MESSAGE));
    }
}
