package io.mysmarthome.service;

import io.mysmarthome.model.entity.Recipient;

public interface NotificationService {
    void notifyToAll(String msg);

    Recipient insertFirebaseToken(String token);
}