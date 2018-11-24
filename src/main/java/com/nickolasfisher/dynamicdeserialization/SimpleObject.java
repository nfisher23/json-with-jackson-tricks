package com.nickolasfisher.dynamicdeserialization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleObject {

    @JsonProperty("property1")
    private String property1;

    @JsonProperty("property2")
    private String property2;

    public String getProperty1() {
        return property1;
    }

    public String getProperty2() {
        return property2;
    }
}
