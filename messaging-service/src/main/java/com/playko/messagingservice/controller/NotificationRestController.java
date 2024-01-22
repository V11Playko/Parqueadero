package com.playko.messagingservice.controller;

import com.playko.messagingservice.configuration.Constants;
import com.playko.messagingservice.entities.SendNotification;
import com.playko.messagingservice.service.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/messaging/v1/")
public class NotificationRestController {
    private final INotificationService notificationService;

    public NotificationRestController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Send notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent", content = @Content),
            @ApiResponse(responseCode = "400", description = "Notification couldn't be sent", content = @Content)
    })
    @PostMapping("/send-notification")
    public ResponseEntity<Map<String, String>> sendNotification(@Valid @RequestBody SendNotification sendNotification) {
        notificationService.sendNotification(sendNotification);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.REGISTERED_OUTPUT_MESSAGE));
    }
}
