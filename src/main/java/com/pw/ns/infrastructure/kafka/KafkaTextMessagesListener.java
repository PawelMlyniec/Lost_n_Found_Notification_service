package com.pw.ns.infrastructure.kafka;

import com.pw.ns.domain.notification.UserId;
import com.pw.ns.domain.textmessage.TextMessageSentEvent;
import com.pw.tms.TextMessageSentProto;
import io.micrometer.core.annotation.Timed;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaTextMessagesListener implements AcknowledgingConsumerAwareMessageListener<String, TextMessageSentProto>, ConsumerSeekAware {

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    KafkaTextMessagesListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @SneakyThrows
    @Override
    @Timed("text.message.listener.time")
    public void onMessage(ConsumerRecord<String, TextMessageSentProto> data, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {

        var proto = data.value();
        var event = TextMessageSentEvent.builder()
            .withMessageId(proto.getMessageId())
            .withSourceUserId(UserId.of(proto.getSourceUserId()))
            .withTargetUserId(UserId.of(proto.getTargetUserId()))
            .withContent(proto.getContent())
            .build();
        eventPublisher.publishEvent(event);
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {

        assignments.keySet().stream()
            .filter(partition -> "tms-text-messages-proto".equals(partition.topic()))
            .forEach(partition -> seek(callback, partition));
    }

    private void seek(ConsumerSeekCallback callback, TopicPartition partition) {

        if (groupId.equals("50")) {
            callback.seekToBeginning("tms-text-messages-proto", partition.partition());
        }
        else if (groupId.equals("60")) {
            callback.seekToEnd("tms-text-messages-proto", partition.partition());
        }
    }
}
