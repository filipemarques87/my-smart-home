package io.mysmarthome.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@IdClass(DeviceDataId.class)
@Table(name = "device_data")
public class DeviceDataEntity {

    @Id
    @Column(name = "device_id", updatable = false, nullable = false)
    private String deviceId;

    @Id
//    @Convert(converter = DateConverter.class)
    @Column(name = "event_time")
    private Instant eventTime;

    @Lob
    @Column(name = "data")
    private String serializedData;

    private transient Object data;

    @Column(name = "type")
    private String type;
}
