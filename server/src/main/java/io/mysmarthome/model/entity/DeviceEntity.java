package io.mysmarthome.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mysmarthome.device.Device;
import io.mysmarthome.model.entity.converter.CustomInfoConverter;
import io.mysmarthome.util.TypedValue;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "device")
public class DeviceEntity implements Device {

    @Id
    @Column(name = "device_id", updatable = false, nullable = false)
    private String deviceId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "device_group_id", referencedColumnName = "id")
    private DeviceGroupEntity group;

    @Column(name = "platform")
    private String platform;

    @Column(name = "name", nullable = false)
    private String name;

    @LazyCollection(LazyCollectionOption.FALSE)
    // to solve Caused by: org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
    @OneToMany(mappedBy = "deviceEntity", cascade = CascadeType.ALL /*, fetch = FetchType.EAGER*/)
    private List<SchedulerEntity> schedulers;
    public List<SchedulerEntity> getSchedulers() {
        if (schedulers == null) {
            schedulers = new ArrayList<>();
        }
        return schedulers;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    // to solve Caused by: org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
    @OneToMany(mappedBy = "deviceEntity", cascade = CascadeType.ALL /*, fetch = FetchType.EAGER*/)
    private List<NotificationEntity> notifications;
    public List<NotificationEntity> getNotifications() {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        return notifications;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "deviceEntity", cascade = CascadeType.ALL)
    private List<ActionEntity> actions;
    public List<ActionEntity> getActions() {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        return actions;
    }

    @Column(name = "additional_info")
    @Convert(converter = CustomInfoConverter.class)
    private Map<String, Object> customInfo = new HashMap<>();

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "units")
    private String units;

    @Override
    public TypedValue getCustomInfo(String key) {
        if (getCustomInfo().containsKey(key)) {
            return new TypedValue(getCustomInfo().get(key));
        }
        return new TypedValue();
    }
}
