package com.quickstarts.kitchensink.service;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@ControllerAdvice
@Slf4j
class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Set<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Set<String> messages = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toSet());

        return ResponseEntity.badRequest().body(messages);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleValidationExceptions(DataIntegrityViolationException ex, Model model) {
        return handleDuplicateEntity();
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleValidationExceptions(DuplicateKeyException ex, Model model) {
        return handleDuplicateEntity();
    }

    private static ResponseEntity<Map<String, String>> handleDuplicateEntity() {
        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("email", "Email Already Registered");
        log.info("Attempt to create a existing member");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObj);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));
        log.info("Validation Error: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

}