package io.mysmarthome.service.notification.firebase;

import io.mysmarthome.service.notification.firebase.impl.FCMException;

public interface FCMService {

    void send(FirebaseNotification notification) throws FCMException;
}
