package com.pw.ns.infrastructure.adapters.kafka;

import com.pw.ns.domain.UserId;
import com.pw.ns.domain.ports.incoming.TextMessageSentEvent;
import com.pw.tms.TextMessageSentProto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTextMessageListener implements MessageListener<String, TextMessageSentProto> {

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    KafkaTextMessageListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onMessage(ConsumerRecord<String, TextMessageSentProto> data) {

        var proto = data.value();
        var event = TextMessageSentEvent.builder()
            .withMessageId(proto.getMessageId())
            .withSourceUserId(UserId.of(proto.getSourceUserId()))
            .withTargetUserId(UserId.of(proto.getTargetUserId()))
            .withContent(proto.getContent())
            .build();
        eventPublisher.publishEvent(event);
    }
}
