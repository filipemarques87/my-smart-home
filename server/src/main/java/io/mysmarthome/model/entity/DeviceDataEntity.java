package io.mysmarthome.model.entity;

import io.mysmarthome.model.entity.converter.DateConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "device_data")
public class DeviceDataEntity {

    @Id
    @Column(name = "device_id", updatable = false, nullable = false)
    private String deviceId;

    @Convert(converter = DateConverter.class)
    @Column(name = "event_time")
    private Date eventTime;

    @Lob
    @Column(name = "data")
    private String serializedData;

    private transient Object data;

    @Column(name = "type")
    private String type;
}
