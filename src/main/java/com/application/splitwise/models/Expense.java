package com.application.splitwise.models;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Expense extends  BaseModel{
    private String description;
    private double amount;
    @ManyToOne
    private SplitwiseGroup splitWiseGroup;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<UserExpense> paidBy;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<UserExpense> shareAmongMembers;
}
