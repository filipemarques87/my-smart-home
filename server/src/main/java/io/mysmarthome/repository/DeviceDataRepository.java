package io.mysmarthome.repository;

import io.mysmarthome.model.entity.DeviceDataEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeviceDataRepository extends CrudRepository<DeviceDataEntity, String> {
}
