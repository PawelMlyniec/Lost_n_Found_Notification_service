package com.pw.ns.application;

import com.pw.ns.infrastructure.kafka.KafkaTextMessagesListener;
import com.pw.tms.TextMessageSentProto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
@EnableKafka
public class KafkaConfig {

    private static final String TEXT_MESSAGE_TOPIC = "tms-text-messages-proto";

    @Bean
    public ConcurrentMessageListenerContainer<String, TextMessageSentProto> textMessageListener(ConsumerFactory<String, TextMessageSentProto> consumerFactory,
                                                                                                KafkaTextMessagesListener listener) {

        var containerProps = new ContainerProperties(TEXT_MESSAGE_TOPIC);
        containerProps.setMessageListener(listener);
        var container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
        container.setConcurrency(8);
        return container;
    }
}
