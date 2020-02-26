# Jackson Wrapping & Unwrapping

_Techniques for wrapping and unwrapping data with Jackson; Serialization and deserialization with immutable value classes_

## Background

Typically, when publishing data to a downstream system, the shape of data (the contract) 
is dictated by the team who owns that downstream system. And there are a few common scenarios  
which verbose to deal with in Jackson.

## Unwrapping Top-Level Metadata for Serialization

Typically a payload will have some metadata which is at the top level of the payload 
as opposed to being in its own `metadata` object. The challenge here is that it makes 
sense to model the metadata as its own object, `Metadata.java`. Luckily, Jackson provides 
a mechanism to keep the metadata encapsulated in its own value class while unwrapping its 
properties for serialization, namely, `@JsonUnwrapped`.

See `com.davidagood.jackson.Payload`. The `Metadata metadata` field requires one additional 
annotation, `@JsonProperty(access = JsonProperty.Access.READ_ONLY)`. 
See background on why this is required [here](https://github.com/FasterXML/jackson-databind/issues/1497) 

### Wrapping Data in Its Own Object

This is the inverse of the "unwrapping" scenario described above. Here, we want to 
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

However, creating a `Data` value class with one field, `List<User> users` is not 
desirable. Jackson doesn't have a an unwrapped annotation. But there is a fairly 
straightforward solution. See the method annotated `@JsonGetter("data")` in 
`com.davidagood.jackson.Payload`.

### Supporting Deserialization with These Techniques

In order to support deserialization for the two techniques above, some extra 
work is required. In this case, we are using immutable value classes. See the  
solution in the `@JsonCreator`-annotated constructor in `com.davidagood.jackson.Payload`.

For the unwrapped metadata, we have to manually pluck the `Metadata` properties 
and then manually wrap them up by constructing a new `Metadata` object.

For the serialization wrapping scenario, the constructor takes a `Map` which represents the 
`data` JSON node. Then we get the unwrapped `users` array from that `Map`. 

If your using non-immutable value classes (e.g. using Lombok's `@Data`), the solution  
involves using `@JsonSetter` methods, similar to the `@JsonCreator`-annotated constructor.

## Running Locally

See `com.davidagood.jackson.SerializationTests`.

The Spring Boot app is just a shell: 
`com.davidagood.jackson.JacksonWrappingUnwrappingApplication`.


