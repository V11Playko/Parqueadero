package com.playko.parkingservice.controller;

import com.playko.parkingservice.configuration.Constants;
import com.playko.parkingservice.entities.RegistryEntry;
import com.playko.parkingservice.entities.RegistryOut;
import com.playko.parkingservice.service.IRegistryEntryService;
import com.playko.parkingservice.service.IRegistryOutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/partner/v1")
public class PartnerController {
    private final IRegistryEntryService registryEntryService;
    private final IRegistryOutService registryOutService;

    public PartnerController(IRegistryEntryService registryEntryService, IRegistryOutService registryOutService) {
        this.registryEntryService = registryEntryService;
        this.registryOutService = registryOutService;
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
}
