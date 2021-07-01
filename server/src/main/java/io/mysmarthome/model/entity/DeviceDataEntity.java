package io.mysmarthome.model.entity;

import io.mysmarthome.model.entity.converter.DateConverter;
import io.mysmarthome.model.entity.converter.LobSerializerConverter;
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
    private String data;
}
