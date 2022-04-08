package io.mysmarthome.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SocketTextHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println(session.getId());
        System.out.println(session.getUri().getPath());
        if (!sessions.containsKey(session.getId())) {
            sessions.put(session.getId(), new WebSocketSessionAsync(session));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (sessions.containsKey(session.getId())) {
            sessions.remove(session.getId());
        }
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

//        System.out.println(session.getId());
//        String payload = message.getPayload();



        if (!sessions.containsKey(session.getId())) {
            return;
        }
        session =  sessions.get(session.getId());//decorateSession(session);


        String msg = "1".repeat(100_000_000);

        session.sendMessage(new TextMessage(msg));


    }

    protected WebSocketSession decorateSession(WebSocketSession session) {
        return  new ConcurrentWebSocketSessionDecorator(new WebSocketSessionAsync(session),1,1);
    }

}


@RequiredArgsConstructor
class WebSocketSessionAsync implements WebSocketSession {

    private final WebSocketSession delegate;

    private final ExecutorService threadpool = Executors.newSingleThreadExecutor();

    private boolean isSending = false;

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
    public void sendMessage(WebSocketMessage<?> message) throws IOException {

        if (isSending) {
            System.out.println("cannot send message. previous message was not delivery yet");
            return;
        }

        isSending =true;
        threadpool.submit(() -> {
            try {
                delegate.sendMessage(message);
            } catch (IOException e) {
                System.out.println("error on sending");
            } finally {
                isSending =false;
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
}