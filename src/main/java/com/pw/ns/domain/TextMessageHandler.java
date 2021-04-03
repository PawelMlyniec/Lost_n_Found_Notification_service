package com.pw.ns.domain;

import com.pw.ns.domain.ports.incoming.TextMessageSentEvent;
import com.pw.ns.domain.ports.outgoing.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@SuppressWarnings("SpringEventListenerInspection")
@Slf4j
@Service
class TextMessageHandler {

    private final NotificationRepository notificationRepository;

    @Autowired
    TextMessageHandler(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @EventListener
    void onTextMessageSent(TextMessageSentEvent event) {

        log.info("Handling event: {}", event);

        var notification = Notification.builder()
            .withType(NotificationType.TEXT_MESSAGE_RECEIVED)
            .withNotifiedUserId(event.targetUserId())
            .withContent(format("Received text message from %s", event.sourceUserId()))
            .build();
        var persistedNotification = notificationRepository.save(notification);

        // TODO send notification email
    }
}
