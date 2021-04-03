package com.pw.ns.infrastructure.config;

import com.pw.ns.infrastructure.adapters.kafka.KafkaTextMessageListener;
import com.pw.tms.TextMessageSentProto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.GenericMessageListenerContainer;

@Configuration
@EnableKafka
public class KafkaConfig {

    private static final String TEXT_MESSAGE_TOPIC = "tms-text-messages-proto";

    @Bean
    public GenericMessageListenerContainer<String, TextMessageSentProto> textMessageListener(ConsumerFactory<String, TextMessageSentProto> consumerFactory,
                                                                                             KafkaTextMessageListener listener) {

        var containerProps = new ContainerProperties(TEXT_MESSAGE_TOPIC);
        containerProps.setMessageListener(listener);

        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
    }
}
