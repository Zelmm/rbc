package com.example.rbc.service.transaction;

import com.example.rbc.dto.TransactionDTO;
import com.example.rbc.service.AbstractKafkaService;
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
public class TransactionKafkaService implements AbstractKafkaService<TransactionDTO> {

    private final KafkaTemplate<Long, String> kafkaTransactionTemplate;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kafka.topics.rbc.transactions.report}")
    private String transactionReportTopic;

    @Autowired
    public TransactionKafkaService(KafkaTemplate<Long, String> kafkaTransactionTemplate,
                                   TransactionService transactionService) {
        this.kafkaTransactionTemplate = kafkaTransactionTemplate;
        this.transactionService = transactionService;
    }

    @Override
    public TransactionDTO save(TransactionDTO dto) {
        return null;
    }

    @Override
    public void send(int id, boolean isValidAmount) {
        log.info("transaction: {}, valid:{}", id, isValidAmount);
        kafkaTransactionTemplate.send(transactionReportTopic, String.format("transaction: %s, valid:%s", id, isValidAmount));
    }

    @Override
    @KafkaListener(id = "Transaction", topics = {"rbc-transactions"}, containerFactory = "singleFactory")
    public void consume(TransactionDTO dto) {
        log.info("=> consumed {}", writeValueAsString(dto));
        var id = dto.getId();
        var dtoFromEntity = transactionService.getDtoById(id);
        send(id, dto.getPAmount() == dtoFromEntity.getPAmount());
    }

    @Override
    @KafkaListener(id = "Transactions", topics = {"rbc-transactions"}, containerFactory = "batchFactory")
    public void consumeBatch(List<TransactionDTO> dtos) {
        log.info("=> consumed {}", dtos.stream().map(this::writeValueAsString).collect(Collectors.joining("; ")));
        var ids = dtos.stream().map(TransactionDTO::getId).collect(Collectors.toList());
        var dtosFromEntity = transactionService.getStoListByIds(ids)
                .stream()
                .collect(Collectors.toMap(TransactionDTO::getId, transactionDTO -> transactionDTO));

        for (var dto : dtos) {
            var id = dto.getId();
            send(id, dto.getPAmount() == dtosFromEntity.get(id).getPAmount());
        }
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
