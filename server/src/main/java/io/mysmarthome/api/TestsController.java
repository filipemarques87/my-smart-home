package io.mysmarthome.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class TestsController {

    private final Map<String, Object> data = new HashMap<>();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @GetMapping("/sensor/{sensorName}")
    public Object handleSensor(@PathVariable("sensorName") String sensorName) {
        log.info("Receive read sensor request {}", sensorName);
        String val = df.format(new Random().nextDouble() * 100);
        return val;
    }

    @PostMapping(value="/switch/{switchName}", consumes = org.springframework.http.MediaType.TEXT_PLAIN_VALUE, produces=org.springframework.http.MediaType.TEXT_PLAIN_VALUE)
    public Object handleSwitchPost(@PathVariable("switchName") String switchName, @RequestBody(required = false) String msg) throws InterruptedException, ExecutionException, TimeoutException {
        log.info("Receive switch action request {}: {}", switchName, msg);
        if (msg == null || msg == "") {
            return data.get(switchName);
        }
        data.put(switchName, msg);
        return msg;
    }
}

