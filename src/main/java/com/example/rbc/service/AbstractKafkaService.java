package com.example.rbc.service;

public interface AbstractKafkaService {
    void send(int id, boolean isValidAmount);

    void consume(String dto);
}
