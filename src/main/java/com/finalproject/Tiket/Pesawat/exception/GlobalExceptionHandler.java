package com.finalproject.Tiket.Pesawat.exception;

import com.finalproject.Tiket.Pesawat.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionHandling.class)
    public ResponseEntity<?> notFound(ExceptionHandling ex) {
        ErrorDTO response = new ErrorDTO();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Other exception handlers
    @ExceptionHandler(UnauthorizedHandling.class)
    public ResponseEntity<?> unAuthorized(UnauthorizedHandling ex) {
        ErrorDTO response = new ErrorDTO();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailAlreadyRegisteredHandling.class)
    public ResponseEntity<?> emailAlreadyRegistered() {
        ErrorDTO response = new ErrorDTO();
        response.setMessage("Email Already Registered");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerHandling.class)
    public ResponseEntity<?> handleInternalServerError(InternalServerHandling ex) {
        ErrorDTO response = new ErrorDTO();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

