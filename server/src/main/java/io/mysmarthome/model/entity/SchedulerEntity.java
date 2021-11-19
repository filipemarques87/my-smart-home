package io.mysmarthome.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "scheduler")
public class SchedulerEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "trigger", nullable = false)
    private String trigger;

    @Column(name = "payload")
    private String serializedPayload;

    @Column(name = "type")
    private String type;

    private transient Object payload;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private DeviceEntity deviceEntity;
}
