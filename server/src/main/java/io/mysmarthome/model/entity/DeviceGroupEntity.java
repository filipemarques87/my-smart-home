package io.mysmarthome.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.text.Normalizer;
import java.util.Locale;

@Data
@Entity
@Table(name = "device_group")
public class DeviceGroupEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "group_id", nullable = false)
    private String groupId;

    @Column(name = "name", nullable = false)
    private String name;

    public void setName(String name) {
        this.name = name;
        this.groupId = Normalizer.normalize(name, Normalizer.Form.NFD)
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", "-");
    }

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "group")
    private DeviceEntity deviceEntity;
}
