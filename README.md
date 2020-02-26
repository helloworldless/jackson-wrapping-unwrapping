# Jackson Wrapping & Unwrapping

_Techniques for wrapping and unwrapping data with Jackson; Serialization and deserialization with immutable value classes_

## Background

Typically, when publishing data to a downstream system, the shape of data (the contract) 
is dictated by the team who owns that downstream system. And there are a few common scenarios 
which are cumbersome to handle with Jackson.

## Unwrapping Top-Level Metadata for Serialization

Typically a payload will have some metadata which is at the top level of the payload 
as opposed to being in its own `metadata` object. The challenge here is that it makes 
sense to model the metadata as its own object, `Metadata.java`. Luckily, Jackson provides 
a mechanism to keep the metadata encapsulated in its own value class while unwrapping its 
properties for serialization, namely, `@JsonUnwrapped`.

From `com.davidagood.jackson.Payload`:

```java
@JsonUnwrapped
@JsonProperty(access = READ_ONLY)
private Metadata metadata;
```

See discussion on why the additional annotation, 
`@JsonProperty(access = JsonProperty.Access.READ_ONLY)` 
is required [here](https://github.com/FasterXML/jackson-databind/issues/1497).

### Wrapping Data in Its Own Object

This is the inverse of the unwrapping scenario described above. Here, we want to 
wrap the payload data in its own `data` object. In other words, the payload data will 
have a shape like this:

```text
{
  ...
  "data": {
    "users": [...]
  }
}
```

However, creating a `Data` value class with only one field, `List<User> users`, is not 
desirable. Jackson doesn't have a an unwrapped annotation. But there is a fairly 
straightforward solution. 

From `com.davidagood.jackson.Payload`:

```java
@JsonGetter("data")
public Map<String, List<User>> getWrappedUsersForSerialization() {
    return Map.of("users", this.users);
}
```

Note that we also have to tell Jackson to ignore the field and make sure it doesn't 
have a getter:

```java
@JsonIgnore
@Getter(AccessLevel.NONE)
private List<User> users;
```

### Supporting Deserialization with These Techniques

In order to support deserialization for the two techniques above, some extra 
work is required. In this case, we are using immutable value classes.

For the unwrapped metadata, we have to manually pluck the `Metadata` properties 
and then manually wrap them up by constructing a new `Metadata` object.

For the serialization wrapping scenario, the constructor takes a `Map` which represents the 
`data` JSON node. Then we get the unwrapped `users` array from that `Map`.

From `com.davidagood.jackson.Payload`:

```java
@JsonCreator
public Payload(@JsonProperty("appId") int appId,
               @JsonProperty("appName") String appName,
               @JsonProperty("data") Map<String, List<User>> data) {
    this.metadata = new Metadata(appId, appName);
    this.users = data.get("users");
}
```

If you're using non-immutable value classes (e.g. using Lombok's `@Data`), the solution  
involves using `@JsonSetter` methods, similar to the `@JsonCreator`-annotated constructor.

## Running Locally

See `com.davidagood.jackson.SerializationTests`.

The Spring Boot app is just a shell: 
`com.davidagood.jackson.JacksonWrappingUnwrappingApplication`.


