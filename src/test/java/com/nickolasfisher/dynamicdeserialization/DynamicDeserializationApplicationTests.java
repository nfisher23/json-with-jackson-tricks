package com.nickolasfisher.dynamicdeserialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDeserializationApplicationTests {

    @Autowired
    ObjectMapper objectMapper;

    public static final String NORMAL_RESPONSE = "{\"hasErrors\":false,\"body\":{\"property1\":\"value1\",\"property2\":\"value2\"}}";
    public static final String RESPONSE_WITH_ERRORS = "{\"hasErrors\":true,\"body\":[{\"errorMessage\":\"you totally messed this up\"},{\"errorMessage\":\"seriously, that was pretty whack\"}]}";

    @Test
    public void normalResponse_setsBodyIsObject() throws Exception {
        DynamicResponseObject dynamicResponseObject = objectMapper.readValue(NORMAL_RESPONSE, DynamicResponseObject.class);

        assertTrue(dynamicResponseObject.getBodyAsNode().isObject());
    }

    @Test
    public void abnormalResponse_setsBodyIsArray() throws Exception {
        DynamicResponseObject dynamicResponseObject = objectMapper.readValue(RESPONSE_WITH_ERRORS, DynamicResponseObject.class);

        assertTrue(dynamicResponseObject.getBodyAsNode().isArray());
    }

    @Test
    public void normalResponse_accessNodeDynamically() throws Exception {
        DynamicResponseObject dynamicResponseObject = objectMapper.readValue(NORMAL_RESPONSE, DynamicResponseObject.class);

        JsonNode bodyNode = dynamicResponseObject.getBodyAsNode();

        assertEquals("value1", bodyNode.get("property1").asText());
        assertEquals("value2", bodyNode.get("property2").asText());
    }

    @Test
    public void abnormalResponse_accessNodesDynamically() throws Exception {
        DynamicResponseObject dynamicResponseObject = objectMapper.readValue(RESPONSE_WITH_ERRORS, DynamicResponseObject.class);

        JsonNode bodyNode = dynamicResponseObject.getBodyAsNode();

        assertEquals("you totally messed this up", bodyNode.get(0).get("errorMessage").asText());
        assertEquals("seriously, that was pretty whack", bodyNode.get(1).get("errorMessage").asText());
    }

    @Test
    public void normalResponse_actuallyDeserializes() throws Exception {
        DynamicResponseObject dynamicResponseObject = objectMapper.readValue(NORMAL_RESPONSE, DynamicResponseObject.class);

        SimpleObject simpleObjectDeserialized = dynamicResponseObject.getBodyAsSimpleObject();

        assertEquals("value1", simpleObjectDeserialized.getProperty1());
        assertEquals("value2", simpleObjectDeserialized.getProperty2());
    }

    @Test
    public void abnormalResponse_actuallyDeserializes() throws Exception {
        DynamicResponseObject dynamicResponseObject = objectMapper.readValue(RESPONSE_WITH_ERRORS, DynamicResponseObject.class);

        List<Error> errors = dynamicResponseObject.getErrors();

        assertEquals(2, errors.size());
        assertEquals("you totally messed this up", errors.get(0).getErrorMessage());
        assertEquals("seriously, that was pretty whack", errors.get(1).getErrorMessage());
    }
}
