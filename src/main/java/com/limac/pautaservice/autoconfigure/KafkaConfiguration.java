package com.limac.pautaservice.autoconfigure;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuração de Kafka.
 */
@Configuration
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaConfiguration {

    private final String topic;
    private final String bootstrapServers;

    public KafkaConfiguration(@Value(value = "${kafka.topic}") String topic, @Value(value = "${kafka.bootstrapServers}") String bootstrapServers) {
        this.topic = topic;
        this.bootstrapServers = bootstrapServers;
    }

    /**
     * Configura um {@link KafkaAdmin} bean.
     *
     * @return {@link KafkaAdmin} bean.
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        final Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(config);
    }

    /**
     * Configura um {@link NewTopic} bean.
     *
     * @return {@link NewTopic} bean.
     */
    @Bean
    public NewTopic newTopic() {
        return new NewTopic(topic, 1, (short) 1);
    }

    /**
     * Configura uma {@link ProducerFactory} bean.
     *
     * @return {@link ProducerFactory} bean.
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        final Map<String, Object> configs = new HashMap<>();

        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configs);
    }

    /**
     * Configura um {@link KafkaTemplate} bean.
     *
     * @return {@link KafkaTemplate} bean.
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
