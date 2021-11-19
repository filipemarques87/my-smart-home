package io.mysmarthome.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/check")
public class CheckController {

    @GetMapping(value = "/ping")
    public Map<String, String> pingServer() {
        log.info("Receive ping");
        return Map.of("val", "hello my smart home");
    }
}

