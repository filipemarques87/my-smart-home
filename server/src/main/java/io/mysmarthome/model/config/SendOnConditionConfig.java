package io.mysmarthome.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class SendOnConditionConfig {

    @JsonProperty("triggers")
    private Set<String> triggers;

    @JsonProperty("condition")
    private String condition;
}
