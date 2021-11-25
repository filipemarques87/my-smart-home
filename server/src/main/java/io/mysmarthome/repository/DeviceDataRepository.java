package io.mysmarthome.repository;

import io.mysmarthome.model.entity.DeviceDataEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceDataRepository extends CrudRepository<DeviceDataEntity, String> {

    List<DeviceDataEntity> findAllByDeviceId(String deviceId, Pageable pageable);
}
