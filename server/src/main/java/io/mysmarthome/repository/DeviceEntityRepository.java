package io.mysmarthome.repository;

import io.mysmarthome.model.entity.DeviceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceEntityRepository extends CrudRepository<DeviceEntity, String> {

    List<DeviceEntity> findByGroup_groupId(String groupId);
}
