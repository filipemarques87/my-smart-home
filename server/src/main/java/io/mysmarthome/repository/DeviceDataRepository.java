package io.mysmarthome.repository;

import io.mysmarthome.model.entity.DeviceDataEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceDataRepository extends CrudRepository<DeviceDataEntity, String> {
}
