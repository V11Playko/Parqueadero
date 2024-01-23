package com.playko.parkingservice.controller;

import com.playko.parkingservice.configuration.Constants;
import com.playko.parkingservice.entities.Parking;
import com.playko.parkingservice.entities.RegistryEntry;
import com.playko.parkingservice.entities.VehicleRegistrations;
import com.playko.parkingservice.service.IParkingService;
import com.playko.parkingservice.service.IRegistryEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin/v1")
public class AdminController {
    private final IParkingService parkingService;
    private final IRegistryEntryService registryEntryService;

    public AdminController(IParkingService parkingService, IRegistryEntryService registryEntryService) {
        this.parkingService = parkingService;
        this.registryEntryService = registryEntryService;
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
    @PutMapping("/update-parking/{id}")
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
    @DeleteMapping("/delete-parking/{id}")
    public ResponseEntity<Map<String, String>> deleteParkingById(@PathVariable Long id) {
        parkingService.deleteParking(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.PARKING_DELETE_MESSAGE));
    }


    @Operation(summary = "Get vehicles from the specified parking lot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicles list returned", content = @Content),
            @ApiResponse(responseCode = "409", description = "Vehicles already exists", content = @Content)
    })
    @GetMapping("/list-specific-parking-vehicles")
    public ResponseEntity<List<RegistryEntry>> getListSpecificParkingVehicles(
            @RequestParam("parkingId") Long parkingId
    ) {
        return ResponseEntity.ok(registryEntryService.getListSpecificParkingVehicles(parkingId));
    }

    @Operation(summary = "Get top vehicles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List of top vehicles", content = @Content)
    })
    @GetMapping("/get-top-vehicles")
    public ResponseEntity<List<VehicleRegistrations>> getTopVehicles() {
        return ResponseEntity.ok(registryEntryService.getTopVehiclesByRegistrations());
    }

    @Operation(summary = "Get top vehicles in specific parking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List of top vehicles", content = @Content)
    })
    @GetMapping("/get-top-vehicles-in-parking/{id}")
    public ResponseEntity<List<VehicleRegistrations>> getTopVehiclesInParking(
            @PathVariable Long id) {
        List<VehicleRegistrations> topVehicles = registryEntryService.getTopVehiclesByRegistrationsInParking(id);
        return ResponseEntity.ok(topVehicles);
    }

    @Operation(summary = "Get vehicles in parking lot for the first time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicles in parking lot for the first time", content = @Content)
    })
    @GetMapping("/first-time-parkings/{id}")
    public ResponseEntity<List<String>> getFirstTimeParkings(@PathVariable Long id) {
        List<String> firstTimeParkings = parkingService.getFirstTimeParkings(id);
        return new ResponseEntity<>(firstTimeParkings, HttpStatus.OK);
    }

    @Operation(summary = "Get plate of the vehicles found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plate found", content = @Content)
    })
    @GetMapping("/search-plate")
    public ResponseEntity<List<RegistryEntry>> searchParkedVehiclesByPlateNumber(
            @RequestParam String plateNumber) {
        return ResponseEntity.ok(registryEntryService.findParkedVehiclesByPlateNumber(plateNumber));
    }
}
