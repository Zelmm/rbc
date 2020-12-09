package com.example.rbc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class TransactionDTO extends AbstractDto{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double pAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long pData;
}
