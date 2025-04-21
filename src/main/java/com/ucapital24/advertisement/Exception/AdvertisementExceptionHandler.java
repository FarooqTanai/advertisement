package com.ucapital24.advertisement.Exception;

import com.ucapital24.advertisement.model.ErrorResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdvertisementExceptionHandler {


    @ExceptionHandler(AdvertisementNotFoundException.class)
    public ResponseEntity handleAdvertisementNotFoundException(AdvertisementNotFoundException ex) {
        ErrorResponseBody  errorResponseBody = new ErrorResponseBody(ErrorCode.ADVERTISEMENT_NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleValidationException(ValidationException ex) {
        ErrorResponseBody  errorResponseBody = new ErrorResponseBody(ErrorCode.VALIDATION_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity handleGenericException(GenericException ex) {
        ErrorResponseBody  errorResponseBody = new ErrorResponseBody(ErrorCode.GENERIC_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public enum ErrorCode {
        ADVERTISEMENT_NOT_FOUND,
        VALIDATION_ERROR,
        GENERIC_ERROR
    }
}
