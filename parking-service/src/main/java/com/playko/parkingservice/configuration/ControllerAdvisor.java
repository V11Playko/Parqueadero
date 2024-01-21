package com.playko.parkingservice.configuration;

import com.playko.parkingservice.service.exceptions.CostPerHourIsRequired;
import com.playko.parkingservice.service.exceptions.InvalidAssignedPartnerException;
import com.playko.parkingservice.service.exceptions.MaximumCapacityIsRequired;
import com.playko.parkingservice.service.exceptions.NameIsRequired;
import com.playko.parkingservice.service.exceptions.NoDataFoundException;
import com.playko.parkingservice.service.exceptions.ParkingFullException;
import com.playko.parkingservice.service.exceptions.PlateAlreadyExistsException;
import com.playko.parkingservice.service.exceptions.PlateNotRegisteredException;
import com.playko.parkingservice.service.exceptions.UserIsNotPartnerException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.playko.parkingservice.configuration.Constants.COSTPERHOUR_IS_REQUIRED_MESSAGE;
import static com.playko.parkingservice.configuration.Constants.INVALID_ASSIGNED_PARTNER_MESSAGE;
import static com.playko.parkingservice.configuration.Constants.MAXIMUMCAPACITY_IS_REQUIRED_MESSAGE;
import static com.playko.parkingservice.configuration.Constants.NAME_IS_REQUIRED_MESSAGE;
import static com.playko.parkingservice.configuration.Constants.NO_DATA_FOUND_MESSAGE;
import static com.playko.parkingservice.configuration.Constants.PARKING_FULL_MESSAGE;
import static com.playko.parkingservice.configuration.Constants.PLATE_ALREADY_EXISTS_MESSAGE;
import static com.playko.parkingservice.configuration.Constants.PLATE_NOT_REGISTERED_MESSAGE;
import static com.playko.parkingservice.configuration.Constants.RESPONSE_MESSAGE_KEY;
import static com.playko.parkingservice.configuration.Constants.USER_IS_NOT_PARTNER_MESSAGE;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleBindExceptions(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(UserIsNotPartnerException.class)
    public ResponseEntity<Map<String, String>> handleUserIsNotPartnerException(
            UserIsNotPartnerException userIsNotPartnerException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_MESSAGE_KEY, USER_IS_NOT_PARTNER_MESSAGE));
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(
            NoDataFoundException noDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_MESSAGE_KEY, NO_DATA_FOUND_MESSAGE));
    }

    @ExceptionHandler(InvalidAssignedPartnerException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAssignedPartnerException(
            InvalidAssignedPartnerException invalidAssignedPartnerException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_MESSAGE_KEY, INVALID_ASSIGNED_PARTNER_MESSAGE));
    }

    @ExceptionHandler(NameIsRequired.class)
    public ResponseEntity<Map<String, String>> nameIsRequiredException(
            NameIsRequired nameIsRequiredException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_MESSAGE_KEY, NAME_IS_REQUIRED_MESSAGE));
    }

    @ExceptionHandler(MaximumCapacityIsRequired.class)
    public ResponseEntity<Map<String, String>> maximumCapacityIsRequiredException(
            MaximumCapacityIsRequired maximumCapacityIsRequiredException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_MESSAGE_KEY, MAXIMUMCAPACITY_IS_REQUIRED_MESSAGE));
    }

    @ExceptionHandler(CostPerHourIsRequired.class)
    public ResponseEntity<Map<String, String>> costPerHourIsRequiredException(
            CostPerHourIsRequired costPerHourIsRequiredException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_MESSAGE_KEY, COSTPERHOUR_IS_REQUIRED_MESSAGE));
    }

    @ExceptionHandler(PlateAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handlePlateAlreadyExistsException(
            PlateAlreadyExistsException plateAlreadyExistsException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_MESSAGE_KEY, PLATE_ALREADY_EXISTS_MESSAGE));
    }

    @ExceptionHandler(ParkingFullException.class)
    public ResponseEntity<Map<String, String>> handleParkingFullException(
            ParkingFullException parkingFullException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_MESSAGE_KEY, PARKING_FULL_MESSAGE));
    }

    @ExceptionHandler(PlateNotRegisteredException.class)
    public ResponseEntity<Map<String, String>> handlePlateNotRegisteredException(
            PlateNotRegisteredException plateNotRegisteredException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_MESSAGE_KEY, PLATE_NOT_REGISTERED_MESSAGE));
    }
}
