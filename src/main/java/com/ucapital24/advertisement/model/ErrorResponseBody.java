package com.ucapital24.advertisement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ucapital24.advertisement.Exception.AdvertisementExceptionHandler.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponseBody {

    @JsonProperty("error_code")
    private ErrorCode errorCode;

    @JsonProperty("error_message")
    private String errorMessage;

    public ErrorResponseBody(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage= errorMessage;
    }
}
