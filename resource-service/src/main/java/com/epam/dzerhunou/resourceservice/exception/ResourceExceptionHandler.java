package com.epam.dzerhunou.resourceservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ApplicationException.class})
    protected ResponseEntity<Object> notFoundExceptionHandler(ApplicationException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorMessage", e.getMessage());
        body.put("errorCode", e.getStatus());
        return ResponseEntity.status(e.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }
}
