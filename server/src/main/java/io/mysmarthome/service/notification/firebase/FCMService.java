package io.mysmarthome.service.notification.firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Slf4j
@NoArgsConstructor
@Service
public class FCMService {

    public void send(FirebaseNotification notification) throws FCMException {
        if (FirebaseApp.getApps().isEmpty()) {
            return;
        }

        log.info("Sending notification ...");
        try {
            FirebaseMessaging.getInstance().sendAsync(getPreconfiguredMessage(notification)).get();
        } catch (InterruptedException e) {
            log.error("Message not sent", e);
        } catch (ExecutionException e) {
            if (failDueInvalidToken(e)) {
                throw new FCMException(e);
            }
        }
    }

    private boolean failDueInvalidToken(Throwable t) {
        if (t == null) {
            return false;
        } else if (t.getMessage().equals("The registration token is not a valid FCM registration token")) {
            return true;
        }
        return failDueInvalidToken(t.getCause());
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis())
                .setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setSound("default"/* NotificationParameter.SOUND.getValue() */)
//						 .setColor("#FFFF00"/*NotificationParameter.COLOR.getValue()*/)
                        .setTag(topic)
                        .setIcon("ic_stat_sentiment_neutral").build())
                .build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setCategory(topic)
                        .setThreadId(topic)
                        .build())
                .build();
    }

    private Message getPreconfiguredMessage(FirebaseNotification request) {
//		Map<String, String> map = new HashMap<>();
//		map.put("message", "adeus mundo");

        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .setToken(request.getToken())
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getContent())
                        .build())
                // .putAllData(map)
                // .putData("message", "value")//
                .build();
    }
}
