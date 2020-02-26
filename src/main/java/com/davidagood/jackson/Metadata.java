package com.davidagood.jackson;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app-config")
@ConstructorBinding
@Value
@NonFinal
@Validated
public class Metadata {

    @Min(1)
    private int appId;

    @NotBlank
    private String appName;
}
