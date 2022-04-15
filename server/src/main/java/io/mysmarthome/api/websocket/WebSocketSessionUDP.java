package io.mysmarthome.api.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
public class WebSocketSessionUDP implements WebSocketSession {

    private final WebSocketSession delegate;

    private final ExecutorService threadPool = Executors.newSingleThreadExecutor();

    private AtomicBoolean isSending = new AtomicBoolean(false);

    private AtomicBoolean canSend = new AtomicBoolean(false);

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public URI getUri() {
        return delegate.getUri();
    }

    @Override
    public HttpHeaders getHandshakeHeaders() {
        return delegate.getHandshakeHeaders();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Principal getPrincipal() {
        return delegate.getPrincipal();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return delegate.getLocalAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return delegate.getRemoteAddress();
    }

    @Override
    public String getAcceptedProtocol() {
        return delegate.getAcceptedProtocol();
    }

    @Override
    public void setTextMessageSizeLimit(int messageSizeLimit) {
        delegate.setTextMessageSizeLimit(messageSizeLimit);
    }

    @Override
    public int getTextMessageSizeLimit() {
        return delegate.getTextMessageSizeLimit();
    }

    @Override
    public void setBinaryMessageSizeLimit(int messageSizeLimit) {
        delegate.setBinaryMessageSizeLimit(messageSizeLimit);
    }

    @Override
    public int getBinaryMessageSizeLimit() {
        return delegate.getBinaryMessageSizeLimit();
    }

    @Override
    public List<WebSocketExtension> getExtensions() {
        return delegate.getExtensions();
    }

    @Override
    public void sendMessage(WebSocketMessage<?> message) {
        if (isSending.get() || !canSend.get()) {
            return;
        }

        isSending.set(true);
        canSend.set(false);
        threadPool.submit(() -> {
            try {
                delegate.sendMessage(message);
            } catch (Exception e) {
                log.error("Error on sending message");
            } finally {
                isSending.set(false);
            }
        });
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    @Override
    public void close(CloseStatus status) throws IOException {
        delegate.close(status);
    }

    public void canSend() {
        canSend.set(true);
    }
}
