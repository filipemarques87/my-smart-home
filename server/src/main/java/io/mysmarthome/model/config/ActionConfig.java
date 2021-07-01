package io.mysmarthome.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class ActionConfig {

    @JsonProperty("trigger")
    private String trigger;

    @JsonProperty("deviceId")
    private String deviceId;

    @JsonProperty("payload")
    private String payload;
}
