package com.example.rbc.service.transaction;

import com.example.rbc.converter.TransactionConverter;
import com.example.rbc.dto.TransactionDTO;
import com.example.rbc.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final TransactionConverter converter = new TransactionConverter();

    @Autowired
    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionDTO getDtoById(int id) {
        return converter.convertEntityToDto(repository.getOne(id));
    }

    public List<TransactionDTO> getStoListByIds(@Param("ids") List<Integer> ids) {
        return converter.convertAllEntityToDtos(repository.getAllByIds(ids));
    }

}
