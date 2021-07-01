package io.mysmarthome.api;

import io.mysmarthome.model.dto.TokenDto;
import io.mysmarthome.model.mapper.TokenMapper;
import io.mysmarthome.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/firebase")
public class FirebaseController {

    private final NotificationService notificationService;
    private final TokenMapper tokenMapper;

    @PostMapping
    public ResponseEntity<TokenDto> addToken(@RequestBody TokenDto token) {
        log.info("Register new firebase token: {}", token);
        return Optional.ofNullable(notificationService.insertFirebaseToken(token.getToken()))
                .map(tokenMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
