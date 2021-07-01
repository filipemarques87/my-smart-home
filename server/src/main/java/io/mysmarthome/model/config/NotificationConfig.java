package io.mysmarthome.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class NotificationConfig {

    @JsonProperty("condition")
    private String condition;

    @JsonProperty("message")
    private String message;
}
