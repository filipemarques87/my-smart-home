package io.mysmarthome.model.mapper;

import io.mysmarthome.model.config.ActionConfig;
import io.mysmarthome.model.config.DeviceConfig;
import io.mysmarthome.model.config.NotificationConfig;
import io.mysmarthome.model.config.SchedulerConfig;
import io.mysmarthome.model.dto.DeviceDto;
import io.mysmarthome.model.dto.DeviceDto.DeviceDtoBuilder;
import io.mysmarthome.model.entity.ActionEntity;
import io.mysmarthome.model.entity.DeviceDataEntity;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.model.entity.DeviceGroupEntity;
import io.mysmarthome.model.entity.NotificationEntity;
import io.mysmarthome.model.entity.SchedulerEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-07-01T05:07:48+0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Ubuntu)"
)
@Component
public class DeviceMapperImpl extends DeviceMapper {

    @Override
    public SchedulerEntity fromConfig(SchedulerConfig schedulerConfig) {
        if ( schedulerConfig == null ) {
            return null;
        }

        SchedulerEntity schedulerEntity = new SchedulerEntity();

        schedulerEntity.setTrigger( schedulerConfig.getTrigger() );
        schedulerEntity.setPayload( schedulerConfig.getPayload() );

        return schedulerEntity;
    }

    @Override
    public ActionEntity fromConfig(ActionConfig deviceConfig) {
        if ( deviceConfig == null ) {
            return null;
        }

        ActionEntity actionEntity = new ActionEntity();

        actionEntity.setTargetId( deviceConfig.getDeviceId() );
        actionEntity.setTrigger( deviceConfig.getTrigger() );
        actionEntity.setPayload( deviceConfig.getPayload() );

        return actionEntity;
    }

    @Override
    public DeviceEntity fromConfig(DeviceConfig deviceConfig) {
        if ( deviceConfig == null ) {
            return null;
        }

        DeviceEntity deviceEntity = new DeviceEntity();

        deviceEntity.setGroup( deviceConfigToDeviceGroupEntity( deviceConfig ) );
        deviceEntity.setDeviceId( deviceConfig.getDeviceId() );
        deviceEntity.setPlatform( deviceConfig.getPlatform() );
        deviceEntity.setName( deviceConfig.getName() );
        deviceEntity.setSchedulers( schedulerConfigListToSchedulerEntityList( deviceConfig.getSchedulers() ) );
        deviceEntity.setNotifications( notificationConfigListToNotificationEntityList( deviceConfig.getNotifications() ) );
        deviceEntity.setActions( actionConfigListToActionEntityList( deviceConfig.getActions() ) );
        Map<String, Object> map = deviceConfig.getCustomInfo();
        if ( map != null ) {
            deviceEntity.setCustomInfo( new HashMap<String, Object>( map ) );
        }
        deviceEntity.setType( deviceConfig.getType() );
        deviceEntity.setUnits( deviceConfig.getUnits() );

        postDeviceFromConfig( deviceEntity );

        return deviceEntity;
    }

    @Override
    public DeviceDto toDto(DeviceEntity device, DeviceDataEntity deviceDataEntity) {
        if ( device == null && deviceDataEntity == null ) {
            return null;
        }

        DeviceDtoBuilder deviceDto = DeviceDto.builder();

        if ( device != null ) {
            deviceDto.deviceId( device.getDeviceId() );
            deviceDto.name( device.getName() );
            deviceDto.type( device.getType() );
            deviceDto.units( device.getUnits() );
        }
        if ( deviceDataEntity != null ) {
            deviceDto.data( deviceDataEntity.getData() );
            if ( deviceDataEntity.getEventTime() != null ) {
                deviceDto.lastUpdate( new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ).format( deviceDataEntity.getEventTime() ) );
            }
        }

        return deviceDto.build();
    }

    protected DeviceGroupEntity deviceConfigToDeviceGroupEntity(DeviceConfig deviceConfig) {
        if ( deviceConfig == null ) {
            return null;
        }

        DeviceGroupEntity deviceGroupEntity = new DeviceGroupEntity();

        deviceGroupEntity.setName( deviceConfig.getGroup() );

        return deviceGroupEntity;
    }

    protected List<SchedulerEntity> schedulerConfigListToSchedulerEntityList(List<SchedulerConfig> list) {
        if ( list == null ) {
            return null;
        }

        List<SchedulerEntity> list1 = new ArrayList<SchedulerEntity>( list.size() );
        for ( SchedulerConfig schedulerConfig : list ) {
            list1.add( fromConfig( schedulerConfig ) );
        }

        return list1;
    }

    protected NotificationEntity notificationConfigToNotificationEntity(NotificationConfig notificationConfig) {
        if ( notificationConfig == null ) {
            return null;
        }

        NotificationEntity notificationEntity = new NotificationEntity();

        notificationEntity.setCondition( notificationConfig.getCondition() );
        notificationEntity.setMessage( notificationConfig.getMessage() );

        return notificationEntity;
    }

    protected List<NotificationEntity> notificationConfigListToNotificationEntityList(List<NotificationConfig> list) {
        if ( list == null ) {
            return null;
        }

        List<NotificationEntity> list1 = new ArrayList<NotificationEntity>( list.size() );
        for ( NotificationConfig notificationConfig : list ) {
            list1.add( notificationConfigToNotificationEntity( notificationConfig ) );
        }

        return list1;
    }

    protected List<ActionEntity> actionConfigListToActionEntityList(List<ActionConfig> list) {
        if ( list == null ) {
            return null;
        }

        List<ActionEntity> list1 = new ArrayList<ActionEntity>( list.size() );
        for ( ActionConfig actionConfig : list ) {
            list1.add( fromConfig( actionConfig ) );
        }

        return list1;
    }
}
