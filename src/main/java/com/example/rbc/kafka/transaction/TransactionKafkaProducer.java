package com.example.rbc.kafka.transaction;

import com.example.rbc.kafka.AbstractKafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class TransactionKafkaProducer extends AbstractKafkaProducer {

    @Bean
    public ProducerFactory<Long, String> producerTransactionFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, String> kafkaTemplate() {
        KafkaTemplate<Long, String> kafkaTemplate = new KafkaTemplate<>(producerTransactionFactory());
        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
        return kafkaTemplate;
    }
}
