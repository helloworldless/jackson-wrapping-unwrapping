package com.davidagood.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class User {

    private int id;
    private String email;

    @JsonCreator
    public static User from(@JsonProperty("id") int id, @JsonProperty("email") String email) {
        return new User(id, email);
    }
}
