package com.example.demo.web;

import com.example.demo.error.RecordNotFoundException;
import com.example.demo.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global error handler class that converts different exceptions thrown in the code into HTTP response messages in JSON format
 */
@ControllerAdvice
@Slf4j
public class GlobalErrorHandler {
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity handleRecordNotFoundException(RecordNotFoundException e) {
        return internalHandleException(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return internalHandleException(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return internalHandleException(HttpStatus.BAD_REQUEST, e, String.format("Invalid value [%s] for %s", e.getValue(), e.getName()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        return internalHandleException(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity internalHandleException(HttpStatus httpStatus, Exception e) {
        return internalHandleException(httpStatus, e, e.getMessage());
    }

    private ResponseEntity internalHandleException(HttpStatus httpStatus, Exception e, String message) {
        log.error("Error processing request", e);
        return ResponseEntity
                .status(httpStatus)
                .body(new ErrorResponse(message));
    }
}
