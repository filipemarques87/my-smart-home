package io.mysmarthome.service.notification.firebase;

public class FCMException extends Exception {
    public FCMException() {
    }

    public FCMException(String message) {
        super(message);
    }

    public FCMException(String message, Throwable cause) {
        super(message, cause);
    }

    public FCMException(Throwable cause) {
        super(cause);
    }

    protected FCMException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
