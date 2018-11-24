package com.nickolasfisher.dynamicdeserialization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DynamicResponseObject {

    @JsonIgnore
    ObjectMapper objectMapper = ApplicationContextProvider.getApplicationContext().getBean(ObjectMapper.class);

    @JsonProperty("hasErrors")
    private boolean hasErrors;

    @JsonIgnore
    private JsonNode bodyAsNode;

    @JsonIgnore
    private SimpleObject simpleObject;

    @JsonIgnore
    private List<Error> errors;

    @JsonProperty("body")
    private void setBody(JsonNode body) {
        this.bodyAsNode = body;
    }

    public JsonNode getBodyAsNode() {
        return this.bodyAsNode;
    }

    public SimpleObject getBodyAsSimpleObject() throws IOException {
        if (simpleObject == null) {
            setSimpleObject();
        }
        return simpleObject;
    }

    private void setSimpleObject() throws IOException {
        if (bodyAsNode.isObject()) {
            simpleObject = objectMapper.readValue(bodyAsNode.toString(), SimpleObject.class);
        } else {
            simpleObject = new SimpleObject();
        }
    }

    public List<Error> getErrors() throws IOException {
        if (errors == null) {
            setErrors();
        }
        return errors;
    }

    private void setErrors() throws IOException {
        if (bodyAsNode.isArray()) {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            JavaType javaType = typeFactory.constructParametricType(List.class, Error.class);
            errors = objectMapper.readValue(bodyAsNode.toString(), javaType);
        } else {
            errors = new ArrayList<>();
        }
    }
}
