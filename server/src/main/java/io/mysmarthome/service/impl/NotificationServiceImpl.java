package io.mysmarthome.service.impl;

import io.mysmarthome.model.entity.Recipient;
import io.mysmarthome.repository.RecipientRepository;
import io.mysmarthome.service.NotificationService;
import io.mysmarthome.service.notification.firebase.FCMException;
import io.mysmarthome.service.notification.firebase.FCMService;
import io.mysmarthome.service.notification.firebase.FirebaseNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final RecipientRepository recipientRepository;
    private final FCMService fcmService;

    @Override
    public void notifyToAll(String msg) {
        recipientRepository.findAll()
                .forEach(r -> notifyRecipient(r, msg));
    }

    private void notifyRecipient(Recipient recipient, String msg) {
        try {
            fcmService.send(FirebaseNotification.builder()
                    .title("SmartHome")
                    .content(msg)
                    .token(recipient.getAddress())
                    .topic("cenas")
                    .build());
        } catch (FCMException e) {
            log.warn("Removing invalid recipient {}", recipient.getAddress());
            recipientRepository.delete(recipient);
        }
    }

    @Override
    public Recipient insertFirebaseToken(String token) {
        Optional<Recipient> optRecipient = recipientRepository.findByAddress(token).stream().findFirst();
        if (optRecipient.isPresent()) {
            log.info("Token {} already registered", token);
            return optRecipient.get();
        }

        Recipient recipient = new Recipient();
        recipient.setAddress(token);
        return recipientRepository.save(recipient);
    }
}
