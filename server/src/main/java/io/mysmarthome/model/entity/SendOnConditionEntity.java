package io.mysmarthome.model.entity;

import io.mysmarthome.model.SendOnConditionTrigger;
import io.mysmarthome.model.entity.converter.SendOnConditionListConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "send_on_condition")
public class SendOnConditionEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "triggers")
    @Convert(converter = SendOnConditionListConverter.class)
    private Set<SendOnConditionTrigger> triggers;

    @Column(name = "condition")
    private String condition;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private DeviceEntity deviceEntity;
}
