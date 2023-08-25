package com.application.splitwise.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettleUpSummaryResource {

    private long id;
    private Member paidBy;
    private Member paidTo;
    private double amount;

    public SettleUpSummaryResource(int i) {
       this.id= i;
    }

}
