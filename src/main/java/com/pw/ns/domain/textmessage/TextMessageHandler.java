package com.pw.ns.domain.textmessage;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringEventListenerInspection")
@Slf4j
@Component
class TextMessageHandler {

    @EventListener
    @Timed("text.message.processing.time")
    void onTextMessageSent(TextMessageSentEvent event) throws InterruptedException {

        log.info("Handling event: {}", event);

        Thread.sleep(20);
    }
}
