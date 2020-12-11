package com.example.rbc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class TransactionDTO extends AbstractDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double pAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long pData;

    @Builder
    public TransactionDTO(@JsonProperty("PID") int id,
                          @JsonProperty("PAMOUNT") double pAmount,
                          @JsonProperty("PDATA") long pData) {
        super(id);
        this.pAmount = pAmount;
        this.pData = pData;
    }
}
