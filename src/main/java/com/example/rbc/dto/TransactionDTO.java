package com.example.rbc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TransactionDTO extends AbstractDto{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double pAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long pData;

    @Builder
    public TransactionDTO(int id, double pAmount, long pData) {
        super(id);
        this.pAmount = pAmount;
        this.pData = pData;
    }
}
