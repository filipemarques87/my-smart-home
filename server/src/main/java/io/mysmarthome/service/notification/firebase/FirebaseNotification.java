package io.mysmarthome.service.notification.firebase;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FirebaseNotification {

    String title;
    String content;
    String topic;
    String token;
}
