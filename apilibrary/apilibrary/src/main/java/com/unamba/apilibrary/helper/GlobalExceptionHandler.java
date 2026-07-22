package com.unamba.apilibrary.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.unamba.apilibrary.dto.response.ResponseGenericMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseGenericMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseGenericMessage response = new ResponseGenericMessage();
        response.error();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            response.listMessage.add(fieldName + ": " + errorMessage);
        });
        
        log.warn("Validation failed: {}", response.listMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseGenericMessage> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ResponseGenericMessage response = new ResponseGenericMessage();
        response.error();
        response.listMessage.add("Error de integridad de datos: recurso duplicado o relacionado incorrectamente.");
        
        log.error("Database integrity violation", ex);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseGenericMessage> handleAllExceptions(Exception ex) {
        ResponseGenericMessage response = new ResponseGenericMessage();
        response.exception();
        response.listMessage.add("Ocurrió un error interno en el servidor. Por favor, intente más tarde.");
        
        log.error("Unhandled exception caught", ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
