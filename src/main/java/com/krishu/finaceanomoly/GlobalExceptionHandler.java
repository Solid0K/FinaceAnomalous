package com.krishu.finaceanomoly;

import com.krishu.finaceanomoly.CustomException.*;
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

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> EmailAlreadyExist(EmailAlreadyExistException exp){
        ErrorResponse errorResponse=new ErrorResponse(409,exp.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> PasswordMismatch(PasswordMismatchException exp){
        ErrorResponse errorResponse=new ErrorResponse(409,exp.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(AccessNotAllowedException.class)
    public ResponseEntity<ErrorResponse> AccessNotAllowed(AccessNotAllowedException exp){
        ErrorResponse errorResponse=new ErrorResponse(403,exp.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvalidDecisionException.class)
    public ResponseEntity<ErrorResponse> InvalidDecision(InvalidDecisionException exp){
        ErrorResponse errorResponse=new ErrorResponse(409,exp.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(OnlyFlaggedReviewException.class)
    public ResponseEntity<ErrorResponse> OnlyFlaggedReview(OnlyFlaggedReviewException exp){
        ErrorResponse errorResponse=new ErrorResponse(409,exp.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(PolicyConflictException.class)
    public ResponseEntity<ErrorResponse> PolicyConflict(PolicyConflictException exp){
        ErrorResponse errorResponse=new ErrorResponse(409,exp.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(PolicyTypeConflictException.class)
    public ResponseEntity<ErrorResponse> PolicyTypeConflict(PolicyTypeConflictException exp){
        ErrorResponse errorResponse=new ErrorResponse(409,exp.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
