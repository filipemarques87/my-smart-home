package io.mysmarthome.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class SchedulerConfig {

    @JsonProperty("trigger")
    private String trigger;

    @JsonProperty("payload")
    private Object payload;
}
