package com.davidagood.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Value
@RequiredArgsConstructor
public class Payload {

    @JsonUnwrapped
    @JsonProperty(access = READ_ONLY)
    private Metadata metadata;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    private List<User> users;

    @JsonCreator
    public Payload(@JsonProperty("appId") int appId,
                   @JsonProperty("appName") String appName,
                   @JsonProperty("data") Map<String, List<User>> data) {
        this.metadata = new Metadata(appId, appName);
        this.users = data.get("users");
    }

    @JsonGetter("data")
    public Map<String, List<User>> getWrappedUsersForSerialization() {
        return Map.of("users", this.users);
    }

}
