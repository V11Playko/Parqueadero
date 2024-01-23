package com.playko.parkingservice.controller;

import com.playko.parkingservice.configuration.Constants;
import com.playko.parkingservice.entities.Parking;
import com.playko.parkingservice.entities.RegistryEntry;
import com.playko.parkingservice.entities.RegistryOut;
import com.playko.parkingservice.entities.VehicleRegistrations;
import com.playko.parkingservice.service.IParkingService;
import com.playko.parkingservice.service.IRegistryEntryService;
import com.playko.parkingservice.service.IRegistryOutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/partner/v1")
public class PartnerController {
    private final IRegistryEntryService registryEntryService;
    private final IRegistryOutService registryOutService;
    private final IParkingService parkingService;

    public PartnerController(IRegistryEntryService registryEntryService, IRegistryOutService registryOutService, IParkingService parkingService) {
        this.registryEntryService = registryEntryService;
        this.registryOutService = registryOutService;
        this.parkingService = parkingService;
    }

    @Operation(summary = "Add registry entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Registry entry created",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Registry entry already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),})
    @PostMapping("/save-registry-entry")
    public ResponseEntity<Map<String, String>> saveRegistryEntry(
            @Valid @RequestBody RegistryEntry registryEntry,
            @RequestParam("parkingId") Long parkingId
    ) {
        registryEntryService.saveRegistryEntry(registryEntry, parkingId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("id", String.valueOf(registryEntry.getId())));
    }

    @Operation(summary = "Add registry out",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Registry out created",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Registry out already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),})
    @PostMapping("/save-registry-out")
    public ResponseEntity<Map<String, String>> saveRegistryOut(
            @Valid @RequestBody RegistryOut registryOut
    ) {
        registryOutService.saveRegistryOut(registryOut);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.REGISTERED_OUTPUT_MESSAGE));
    }

    @Operation(summary = "Get vehicles from the specified parking lot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicles list returned", content = @Content),
            @ApiResponse(responseCode = "409", description = "Vehicles already exists", content = @Content)
    })
    @GetMapping("/list-vehicles")
    public ResponseEntity<List<RegistryEntry>> getListVehicles(
            @RequestParam("parkingId") Long parkingId
    ) {
        return ResponseEntity.ok(registryEntryService.getListVehicles(parkingId));
    }

    @Operation(summary = "Get associated parkings for a partner")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List of associated parkings", content = @Content)
    })@GetMapping("/associated-parkings")
    public ResponseEntity<List<Parking>> getAssociatedParkings(
            @RequestParam("emailAssignedPartner") String emailAssignedPartner) {
            return ResponseEntity.ok(parkingService.getAssociatedParkings(emailAssignedPartner));
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

    @Operation(summary = "Get earnings for period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Earnings for period", content = @Content)
    })
    @GetMapping("/get-earnings-for-period")
    public ResponseEntity<Map<String, Double>> getEarningsForPeriod(
            @RequestParam("parkingId") Long parkingId) {
        return ResponseEntity.ok(parkingService.getEarningsForPeriod(parkingId));
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
