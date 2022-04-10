package io.mysmarthome.config;

import io.mysmarthome.api.websocket.DeviceWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static io.mysmarthome.api.websocket.DeviceWebSocketHandler.BASE_URI;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private DeviceWebSocketHandler deviceWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(deviceWebSocketHandler, BASE_URI + "/*/*");
    }
}
