package io.mysmarthome.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.concurrent.TimeoutException;

@Slf4j
@ControllerAdvice
public class TimeoutExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {TimeoutException.class})
    protected ResponseEntity<Object> handleTimeout(TimeoutException ex, WebRequest request) {
        String message = "Device request timeout";
        log.error(message, ex);
        return handleExceptionInternal(ex, message,
                new HttpHeaders(), HttpStatus.REQUEST_TIMEOUT, request);
    }
}
