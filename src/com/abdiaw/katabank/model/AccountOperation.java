package com.abdiaw.katabank.model;

import com.abdiaw.katabank.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountOperation {
    private String id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private Account bankAccount;
    private String description;
}
