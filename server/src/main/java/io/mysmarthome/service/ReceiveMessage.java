package io.mysmarthome.service;

import io.mysmarthome.device.Device;
import io.mysmarthome.model.entity.ActionEntity;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.model.entity.NotificationEntity;
import io.mysmarthome.platform.message.OnReceive;
import io.mysmarthome.platform.PlatformPlugin;
import io.mysmarthome.platform.message.ReceivedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.SimpleScriptContext;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static io.mysmarthome.util.SneakyException.sneakyException;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReceiveMessage implements OnReceive {

    private final DeviceManager deviceManager;
    private final ScriptEngine scriptEngine;
    private final NotificationService notifierService;
    private final MyPluginManager<PlatformPlugin> platformManager;

    @Override
    public void onReceive(Device device, ReceivedMessage msg) {
        log.info("Receive data for {}: {}", device.getDeviceId(), msg.getMessage().toString());

        deviceManager.saveData(device.getDeviceId(), msg.getReceivedAt(), msg.getMessage());

        Function<String, Object> scriptExecutor = setupScriptEngine(msg.getMessage());
        notify((DeviceEntity) device, scriptExecutor);
        triggerAction((DeviceEntity) device, scriptExecutor);
    }

    private void notify(DeviceEntity device, Function<String, Object> scriptExecutor) {
        device.getNotifications().stream()
                .filter(n -> needToNotify(scriptExecutor, n))
                .forEach(n -> notify(scriptExecutor, n));
    }

    private void triggerAction(DeviceEntity device, Function<String, Object> scriptExecutor) {
        device.getActions().stream()
                .filter(a -> needToPerformAction(scriptExecutor, a))
                .forEach(this::performAction);
    }

    private void performAction(ActionEntity action) {
        log.info("Action triggered for '{}'. Sending action to '{}'", action.getDeviceEntity().getDeviceId(), action.getTargetId());
        Optional<DeviceEntity> deviceOpt = deviceManager.getDevice(action.getTargetId());
        if (deviceOpt.isEmpty()) {
            log.warn("Device id '{}' not found", action.getTargetId());
            return;
        }

        DeviceEntity device = deviceOpt.get();
        platformManager.get(device.getPlatform())
                .send(device, action.getPayload());
    }

    private boolean needToPerformAction(Function<String, Object> scriptExecutor, ActionEntity action) {
        Object output = scriptExecutor.apply(action.getTrigger());
        if (!(output instanceof Boolean)) {
            throw new NotificationException("Notification output must be a boolean value");
        }
        return Boolean.TRUE.equals(output);
    }

    private Function<String, Object> setupScriptEngine(Object val) {
        ScriptContext scriptContext = new SimpleScriptContext();
        scriptContext.setBindings(scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE);
        Bindings engineScope = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);
        engineScope.put("$v", val);
        return sneakyException(trigger -> scriptEngine.eval(trigger, scriptContext));
    }

    private boolean needToNotify(Function<String, Object> scriptExecutor, NotificationEntity notification) {
        Object output = scriptExecutor.apply(notification.getCondition());
        if (!(output instanceof Boolean)) {
            throw new NotificationException("Notification output must be a boolean value");
        }
        return Boolean.TRUE.equals(output);
    }

    private void notify(Function<String, Object> scriptExecutor, NotificationEntity notification) {
        log.info("Send notification for device '{}'", notification.getDeviceEntity().getDeviceId());
        String msg = Objects.toString(scriptExecutor.apply(notification.getMessage()));
        notifierService.notifyToAll(msg);
    }
}
