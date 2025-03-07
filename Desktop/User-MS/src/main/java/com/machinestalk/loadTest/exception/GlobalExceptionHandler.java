/*package com.machinestalk.loadTest.exception;

import com.machinestalk.loadTest.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<MessageResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Error: " + ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MessageResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Error: " + ex.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<MessageResponse> handleUserException(UserException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Error: " + ex.getMessage()));
    }

    // Gestion des erreurs de validation des arguments
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Error: " + errorMsg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleAllExceptions(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Error: An unexpected error occurred"));
    }
}
*/