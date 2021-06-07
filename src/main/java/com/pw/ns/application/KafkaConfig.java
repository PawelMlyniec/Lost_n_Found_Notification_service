package com.pw.ns.application;

import com.pw.lrs.ItemsMatchedProto;
import com.pw.ns.infrastructure.kafka.KafkaLostReportsListener;
import com.pw.ns.infrastructure.kafka.KafkaTextMessagesListener;
import com.pw.tms.TextMessageSentProto;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchemaProvider;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.GenericMessageListenerContainer;

import java.util.List;

@Configuration
@EnableKafka
public class KafkaConfig {

    private static final String TEXT_MESSAGE_TOPIC = "tms-text-messages-proto";
    private static final String LOST_REPORTS_TOPIC = "lrs-lost-reports-proto";

    @Bean
    public GenericMessageListenerContainer<String, TextMessageSentProto> textMessagesListenerContainer(@Qualifier("text-messages") ConsumerFactory<String, TextMessageSentProto> consumerFactory,
                                                                                                       KafkaTextMessagesListener listener) {
        var containerProps = new ContainerProperties(TEXT_MESSAGE_TOPIC);
        containerProps.setMessageListener(listener);
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
    }

    @Bean
    public GenericMessageListenerContainer<String, ItemsMatchedProto> lostReportsListenerContainer(@Qualifier("lost-reports") ConsumerFactory<String, ItemsMatchedProto> consumerFactory,
                                                                                                      KafkaLostReportsListener listener) {
        var containerProps = new ContainerProperties(LOST_REPORTS_TOPIC);
        containerProps.setMessageListener(listener);
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
    }

    @Bean
    @Qualifier("text-messages")
    public ConsumerFactory<String, TextMessageSentProto> textMessagesConsumerFactory(KafkaProperties kafkaProperties,
                                                                                     SchemaRegistryClient schemaRegistry) {
        var props = kafkaProperties.buildConsumerProperties();
        props.put("auto.register.schemas", false);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new KafkaProtobufDeserializer<>(schemaRegistry, props, TextMessageSentProto.class));
    }

    @Bean
    @Qualifier("lost-reports")
    public ConsumerFactory<String, ItemsMatchedProto> lostReportsConsumerFactory(KafkaProperties kafkaProperties,
                                                                                 SchemaRegistryClient schemaRegistry) {
        var props = kafkaProperties.buildConsumerProperties();
        props.put("auto.register.schemas", false);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new KafkaProtobufDeserializer<>(schemaRegistry, props, ItemsMatchedProto.class));
    }

    @Bean
    public SchemaRegistryClient schemaRegistry(KafkaProperties kafkaProperties) {

        var props = kafkaProperties.buildConsumerProperties();
        var schemaRegistryUrl = (String) props.get("schema.registry.url");
        return new CachedSchemaRegistryClient(
            List.of(schemaRegistryUrl),
            Integer.MAX_VALUE,
            List.of(new ProtobufSchemaProvider()),
            props);
    }
}
