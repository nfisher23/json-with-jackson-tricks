package com.nickolasfisher.dynamicdeserialization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    @JsonProperty("errorMessage")
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }
}
