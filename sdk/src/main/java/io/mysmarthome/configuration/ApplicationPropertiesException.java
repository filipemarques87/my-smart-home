package io.mysmarthome.configuration;

public class ApplicationPropertiesException extends RuntimeException {

    public ApplicationPropertiesException() {
    }

    public ApplicationPropertiesException(String message) {
        super(message);
    }

    public ApplicationPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationPropertiesException(Throwable cause) {
        super(cause);
    }

    protected ApplicationPropertiesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
