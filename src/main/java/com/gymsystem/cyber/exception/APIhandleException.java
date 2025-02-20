package com.gymsystem.cyber.exception;

import com.gymsystem.cyber.model.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;

@RestControllerAdvice // canh nhung thang co loi no se bat ngay lap tuc
public class APIhandleException {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleNotAllowException(BadCredentialsException ex) {
        return new ResponseEntity<>("User or paswork not true", HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleDuplicatePhone(AuthException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity handleGlobalException(GlobalException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseObject> handleAccountNotFoundException(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ResponseObject.builder()
                                .message(ex.getMessage())
                                .httpStatus(HttpStatus.UNAUTHORIZED)
                                .build()
                );
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseObject> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ResponseObject.builder()
                                .message(ex.getMessage())
                                .httpStatus(HttpStatus.UNAUTHORIZED)
                                .build()
                );
    }



}


