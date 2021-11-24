package io.mysmarthome.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.mysmarthome.config.exception.ConfigurationException;
import io.mysmarthome.device.Device;
import io.mysmarthome.model.config.DeviceConfig;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.model.mapper.DeviceMapper;
import io.mysmarthome.platform.PlatformPlugin;
import io.mysmarthome.service.DeviceManager;
import io.mysmarthome.service.MyPluginManager;
import io.mysmarthome.service.ReceiveMessage;
import io.mysmarthome.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@RequiredArgsConstructor
@DependsOn("pluginManager")
@Component
public class DeviceInitializer {

    private final DeviceManager deviceManager;
    private final DeviceMapper deviceMapper;
    private final MyPluginManager<? extends PlatformPlugin<? extends Device>> platformManager;
    private final ReceiveMessage receiveMessage;
    private final TaskScheduler scheduler;
    private final SchedulerService schedulerService;

    @PostConstruct
    public void initialize() {
        log.info("Deleting old devices ...");
        deviceManager.cleanDeviceDatabase();
        log.info("... start device configuration ...");
        getDevicesFromConfigFile().stream()
                .map(deviceMapper::fromConfig)
                .map(deviceManager::upsertDevice)
                .forEach(this::configDevice);
        log.info("... device configuration finished");
    }

    private List<DeviceConfig> getDevicesFromConfigFile() {
        try {
            String filename = SystemProperty.DEVICE_FILE.getValue();
            log.info("Read device config file: {}", filename);
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            return mapper.readValue(new File(filename), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Error on reading device config file", e);
            throw new ConfigurationException(e);
        }
    }

    private void configDevice(DeviceEntity deviceEntity) {
        log.info("Configuring device '{}'", deviceEntity.getDeviceId());
        if (!platformManager.exists(deviceEntity.getPlatform())) {
            log.warn("Platform {} does not exists. Device {} skipped", deviceEntity.getPlatform(), deviceEntity.getName());
            return;
        }
        PlatformPlugin<? extends Device> platform = platformManager.get(deviceEntity.getPlatform());
        platform.registerDevice(deviceEntity, receiveMessage);

        if (deviceEntity.getSchedulers() != null) {
            deviceEntity.getSchedulers()
                    .forEach(s -> {
                        log.info("Configuring scheduler for device '{}'", deviceEntity.getDeviceId());
                        scheduler.schedule(() -> {
                            schedulerService.schedulerTriggered(deviceEntity.getDeviceId(), s.getId());
                        }, buildCronTrigger(s.getTrigger()));
                    });
        }
    }

    private Trigger buildCronTrigger(String cronExpr) {
        return new CronTrigger(cronExpr, TimeZone.getTimeZone(TimeZone.getDefault().getID()));
    }
}
