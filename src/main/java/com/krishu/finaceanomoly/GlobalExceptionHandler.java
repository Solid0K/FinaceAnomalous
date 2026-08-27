package com.krishu.finaceanomoly;

import com.krishu.finaceanomoly.CustomException.NotFoundException;
import com.krishu.finaceanomoly.DTO.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> NotFound(NotFoundException exp){
        ErrorResponse errorResponse=new ErrorResponse(404,exp.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
