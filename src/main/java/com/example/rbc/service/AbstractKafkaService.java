package com.example.rbc.service;

import com.example.rbc.dto.AbstractDto;

import java.util.List;

public interface AbstractKafkaService<T extends AbstractDto> {
    T save(T dto);

    void send(T dto);

    void consume(T dto);

    void consumeBatch(List<T> dtos);
}
