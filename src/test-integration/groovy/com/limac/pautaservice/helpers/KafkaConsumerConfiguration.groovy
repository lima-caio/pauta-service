package com.limac.pautaservice.helpers

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConsumerConfiguration {

    @Value(value = '${kafka.topic}')
    String topic

    @Value(value = '${kafka.bootstrapServers}')
    String bootstrapServers

    @Bean
    KafkaConsumer<String, Object> kafkaConsumer() {
        Map<String, Object> configs = [
            (ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG): bootstrapServers,
            (ConsumerConfig.GROUP_ID_CONFIG): 'groupId',
            (ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG): true,
            (ConsumerConfig.AUTO_OFFSET_RESET_CONFIG): 'earliest',
            (ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG): StringDeserializer,
            (ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG): JsonDeserializer,
            (JsonDeserializer.TRUSTED_PACKAGES): '*',
        ]

        KafkaConsumer<String, Object> kafkaConsumer = new KafkaConsumer<>(configs)
        kafkaConsumer.subscribe([topic])
        kafkaConsumer
    }
}
