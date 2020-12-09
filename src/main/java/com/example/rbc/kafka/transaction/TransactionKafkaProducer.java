package com.example.rbc.kafka.transaction;

import com.example.rbc.dto.TransactionDTO;
import com.example.rbc.kafka.AbstractKafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

public class TransactionKafkaProducer extends AbstractKafkaProducer {

    @Bean
    public ProducerFactory<Long, TransactionDTO> producerTransactionFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, TransactionDTO> kafkaTemplate() {
        KafkaTemplate<Long, TransactionDTO> template = new KafkaTemplate<>(producerTransactionFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }
}
