package io.mysmarthome.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> handleSensor(@PathVariable("sensorName") String sensorName) {
        log.info("Receive read sensor request {}", sensorName);
        String val = df.format(new Random().nextDouble() * 10);
        return new ResponseEntity<>(Map.of("val", val), HttpStatus.OK);
    }

    @PostMapping(value="/switch/{switchName}")
    public Object handleSwitchPost(@PathVariable("switchName") String switchName, @RequestBody Map<String,Object> msg) throws InterruptedException, ExecutionException, TimeoutException {
        log.info("Receive switch action request {}: {}", switchName, msg);
        data.putIfAbsent(switchName, Map.of("val", 0));
        if (msg == null || !msg.containsKey("val") || msg.get("val") == "") {
            return data.get(switchName);
        }
        data.put(switchName, msg);
        return msg;
    }
}

