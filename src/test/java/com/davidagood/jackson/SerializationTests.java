package com.davidagood.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SerializationTests {

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    private final String expectedPayloadPath = "expectedPayload.json";
    private final Resource expectedPayloadResource = new ClassPathResource(expectedPayloadPath);

    private static Payload createExpectedPayload() {
        var one = User.from(1, "one@example.com");
        var two = User.from(2, "two@example.com");
        var users = List.of(one, two);
        return new Payload(new Metadata(123, "app-123"), users);
    }

    @Test
    void serialization() throws IOException {
        JsonNode expectedPayload = mapper.readTree(expectedPayloadResource.getFile());
        JsonNode serializedPayload = mapper.valueToTree(createExpectedPayload());
        assertThat(serializedPayload).isEqualTo(expectedPayload);
    }

    @Test
    void deserialization() throws IOException {
        String json = Files.readString(Path.of(expectedPayloadResource.getURI()));
        Payload deserializedPayload = mapper.readValue(json, Payload.class);
        assertThat(deserializedPayload).isEqualTo(createExpectedPayload());
    }

}
