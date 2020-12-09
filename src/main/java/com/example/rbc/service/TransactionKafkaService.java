package com.example.rbc.service;

import com.example.rbc.dto.TransactionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionKafkaService implements AbstractKafkaService<TransactionDTO>{

    private final KafkaTemplate<Long, TransactionDTO> kafkaTransactionTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String transactionReportTopic;

    @Autowired
    public TransactionKafkaService(KafkaTemplate<Long, TransactionDTO> kafkaTransactionTemplate,
                                   @Value("${kafka.topics.rbc.transactions.report}") String transactionReportTopic) {
        this.kafkaTransactionTemplate = kafkaTransactionTemplate;
        this.transactionReportTopic = transactionReportTopic;
    }

    @Override
    public TransactionDTO save(TransactionDTO dto) {
        return null;
    }

    @Override
    public void send(TransactionDTO dto) {
        log.info("<= sending {}", writeValueAsString(dto));
        kafkaTransactionTemplate.send(transactionReportTopic, dto);
    }

    @Override
    @KafkaListener(id = "Transaction", topics = {"rbc-transactions"}, containerFactory = "singleFactory")
    public void consume(TransactionDTO dto) {
        log.info("=> consumed {}", writeValueAsString(dto));
        //сравнить с дто из базы
    }

    @Override
    @KafkaListener(id = "Transaction", topics = {"rbc-transactions"}, containerFactory = "batchFactory")
    public void consumeBatch(List<TransactionDTO> dtos) {
        log.info("=> consumed {}", String.join(
                "; ",
                dtos.stream().map(this::writeValueAsString).collect(Collectors.toList())
        ));
        //сравнить с дто из базы
    }


    private String writeValueAsString(TransactionDTO dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Writing value to JSON failed: " + dto.toString());
        }
    }
}
