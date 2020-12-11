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

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionKafkaService implements AbstractKafkaService {

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
    public void send(int id, boolean isValidAmount) {
        log.info("transaction: {}, valid:{}", id, isValidAmount);
        kafkaTransactionTemplate.send(transactionReportTopic, String.format("transaction: %s, valid:%s", id, isValidAmount));
    }

    @Override
    @KafkaListener(id = "Transaction", topics = {"rbc-transactions"}, containerFactory = "batchFactory")
    public void consume(String dtoString) {
        var dtoList = getTransactionsFromString(dtoString);
        log.info("=> consumed {}", dtoList);

        var ids = dtoList
                .stream()
                .map(TransactionDTO::getId)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() == 1)
                .map(e -> e.getKey())
                .collect(Collectors.toList());

        var dtosFromEntity = transactionService.getDtoListByIds(ids)
                .stream()
                .collect(Collectors.toMap(TransactionDTO::getId, transactionDTO -> transactionDTO));

        for (var dto : dtoList) {
            var id = dto.getId();
            if (dtosFromEntity.containsKey(id)) {
                send(id, dto.getPAmount() == dtosFromEntity.get(id).getPAmount());
            }
        }
    }

    private List<TransactionDTO> getTransactionsFromString(String dtos) {
        List<TransactionDTO> result;
        try {
            result = objectMapper.readValue(dtos, objectMapper.getTypeFactory().constructCollectionType(List.class, TransactionDTO.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("Can't read TransactionDTO from string: {}", dtos);
            result = Collections.emptyList();
        }
        return result;
    }
}
