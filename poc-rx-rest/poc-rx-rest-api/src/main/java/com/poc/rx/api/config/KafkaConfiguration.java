package com.poc.rx.api.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Maksym Khudiakov
 */
@EnableKafka
@Configuration
public class KafkaConfiguration {

    private final Map<String, Object> producerProps;

    @Autowired
    public KafkaConfiguration(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        this.producerProps = producerProps(bootstrapServers);
    }

    private Map<String, Object> producerProps(String bootstrapServers) {
        final Map<String, Object> props = new ConcurrentHashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.BytesSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.BytesSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        return props;
    }

    @Bean
    public ProducerFactory<Bytes, Bytes> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProps);
    }

    @Bean
    public KafkaTemplate<Bytes, Bytes> kafkaTemplate(ProducerFactory<Bytes, Bytes> pf,
                                                       ConcurrentKafkaListenerContainerFactory<Bytes, String> factory) {
        KafkaTemplate<Bytes, Bytes> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }
}