package com.example.rbc.service;

import com.example.rbc.dto.AbstractDto;

import java.util.List;

public interface AbstractKafkaService<T extends AbstractDto> {
    T save(T dto);

    void send(int id, boolean isValidAmount);

    void consume(T dto);

    void consumeBatch(List<T> dtos);
}
