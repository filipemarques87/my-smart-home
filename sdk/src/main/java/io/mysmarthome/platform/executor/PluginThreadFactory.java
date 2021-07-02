package io.mysmarthome.platform.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class PluginThreadFactory implements ThreadFactory {
    private static final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
    private final Thread.UncaughtExceptionHandler handler;

    public PluginThreadFactory() {
        this.handler = new PluginExceptionHandler();
    }

    @Override
    public Thread newThread(Runnable run) {
        Thread thread = defaultFactory.newThread(run);
        thread.setUncaughtExceptionHandler(handler);
        return thread;
    }
}
