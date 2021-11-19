package io.mysmarthome.service;

public interface SchedulerService {

    void schedulerTriggered(String deviceId, String schedulerId);
}
