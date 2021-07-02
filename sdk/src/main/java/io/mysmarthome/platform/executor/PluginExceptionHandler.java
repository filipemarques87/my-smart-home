package io.mysmarthome.platform.executor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PluginExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable t) {
        log.error("Error occurred on mqtt client executor", t);
    }
}
