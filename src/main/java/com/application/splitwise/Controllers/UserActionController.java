package com.application.splitwise.Controllers;


import com.application.splitwise.DTOs.AddExpenseRequestResource;
import com.application.splitwise.Services.ExpenseActionService;
import com.application.splitwise.models.SettleUpSummaryResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserActionController {

    @Autowired
    private ExpenseActionService expenseActionService;

    public void addExpense(AddExpenseRequestResource addExpenseRequestResource )
    {
        expenseActionService.addExpense(addExpenseRequestResource.getGroupId(),addExpenseRequestResource.getAmount(),addExpenseRequestResource.getAmountPaidByUser(),addExpenseRequestResource.getUsersInvolved());
    }
    public  List<SettleUpSummaryResource> checkExpenseStatus(long groupId){

        return expenseActionService.checkExpenseStatus(groupId);
    }
}
