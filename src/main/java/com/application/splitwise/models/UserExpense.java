package com.application.splitwise.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Currency;


@Data
@Entity
public class UserExpense extends BaseModel{

    @NotNull
    private double amount;

    @ManyToOne
    private Member member;

    private ExpenseType expenseType;

    private Currency currency;
}

