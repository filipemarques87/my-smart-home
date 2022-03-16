package io.mysmarthome.model.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class DeviceConfig {

    @JsonProperty("deviceId")
    private String deviceId;

    @JsonProperty("group")
    private String group;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sendOnCondition")
    private List<SendOnConditionConfig> sendOnCondition;

    @JsonProperty("schedulers")
    private List<SchedulerConfig> schedulers;

    @JsonProperty("notifications")
    private List<NotificationConfig> notifications;

    @JsonProperty("actions")
    private List<ActionConfig> actions;

    @JsonProperty("type")
    private String type;

    @JsonIgnore
    private Map<String, Object> customInfo = new HashMap<>();

    @JsonAnySetter
    private void addCustomInfo(String key, Object value) {
        customInfo.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getCustomInfo() {
        return Collections.unmodifiableMap(customInfo);
    }
}
