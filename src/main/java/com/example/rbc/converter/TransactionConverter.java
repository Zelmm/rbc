package com.example.rbc.converter;

import com.example.rbc.dto.TransactionDTO;
import com.example.rbc.entity.TransactionEntity;


public class TransactionConverter implements Converter<TransactionDTO, TransactionEntity> {
    @Override
    public TransactionEntity convertDtoToEntity(TransactionDTO dto) {
        return TransactionEntity.builder()
                .id(dto.getId())
                .amount(dto.getPAmount())
//                .jsonData(dto.getPData())    Вот тут я не понял, как данные из примера из кафки трансформировать в табличные.
                .build();
    }

    @Override
    public TransactionDTO convertEntityToDto(TransactionEntity entity) {
        return TransactionDTO.builder()
                .id(entity.getId())
                .pAmount(entity.getAmount())
//                .pData(entity.getJsonData())    Вот тут я не понял, как данные из примера из кафки трансформировать в табличные.
                .build();
    }
}
