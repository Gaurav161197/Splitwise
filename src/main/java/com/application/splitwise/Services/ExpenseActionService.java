package com.application.splitwise.Services;


import com.application.splitwise.Repository.ExpenseRepository;
import com.application.splitwise.Repository.GroupRepository;
import com.application.splitwise.Repository.UserRepository;
import com.application.splitwise.strategies.SettleUpStrategy;
import com.application.splitwise.models.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ExpenseActionService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    @Qualifier("optimizedSettleUpStrategy")
    private SettleUpStrategy optimizedSettleUpStrategy;


    @Transactional
    public void addExpense(long groupId, double amount, Map<Member, Double> amountPaidByUser, List<Member> usersInvolved) {

        double shareAmount = amount / usersInvolved.size();

        List<UserExpense> paidByList = new ArrayList<>();
        List<UserExpense> oweAmountDistributionList = new ArrayList<>();

        for (Member member : usersInvolved) {
            UserExpense userExpense = new UserExpense();
            userExpense.setAmount(shareAmount);
            userExpense.setExpenseType(ExpenseType.OWE);
            userExpense.setMember(member);
            oweAmountDistributionList.add(userExpense);

        }

        for (Member member : amountPaidByUser.keySet()) {
            UserExpense userExpense = new UserExpense();
            userExpense.setAmount(amountPaidByUser.get(member));
            userExpense.setExpenseType(ExpenseType.Paid);
            userExpense.setMember(member);
            paidByList.add(userExpense);

        }
        SplitwiseGroup splitwiseGroup = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(String.format("Group id %s not present. ", groupId)));
        Expense expense = new Expense();
        expense.setSplitWiseGroup(splitwiseGroup);
        expense.setAmount(amount);
        expense.setPaidBy(paidByList);
        expense.setShareAmongMembers(oweAmountDistributionList);
        expense.setDescription("Added expense for group: " + groupId);

        expenseRepository.save(expense);

        List<Expense> expensesList = splitwiseGroup.getExpensesList();
        expensesList.add(expense);
        groupRepository.save(splitwiseGroup);

        System.out.println("Expense successfully added to the group: " + groupId);
    }

    @Transactional
    public List<SettleUpSummaryResource> checkExpenseStatus(long groupId) {
        SplitwiseGroup splitwiseGroup = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(String.format("Group id %s not present. ", groupId)));
        List<Expense> expensesList = splitwiseGroup.getExpensesList();

        Map<Member, Double> currentExpenseStatusMap = new HashMap<>();
        for (int p = 0; p < expensesList.size(); p++) {
            Expense expense = expensesList.get(p);
            List<UserExpense> paidByList = expense.getPaidBy();
            List<UserExpense> oweByMemberList = expense.getShareAmongMembers();

            for (UserExpense userExpense : paidByList) {
                Member member = userExpense.getMember();
                if (!currentExpenseStatusMap.containsKey(member)) {
                    currentExpenseStatusMap.put(member, userExpense.getAmount());
                } else {

                    double updatedAmount = userExpense.getAmount() + currentExpenseStatusMap.get(member);
                    currentExpenseStatusMap.put(member, updatedAmount);
                }

            }

            for (UserExpense userExpense : oweByMemberList) {

                Member member = userExpense.getMember();
                if (!currentExpenseStatusMap.containsKey(member)) {
                    currentExpenseStatusMap.put(member, userExpense.getAmount());
                } else {
                    double updatedAmount = currentExpenseStatusMap.get(member) - userExpense.getAmount();
                    currentExpenseStatusMap.put(member, updatedAmount);
                }

            }

            if (p == (expensesList.size() - 1)) {
                System.out.println("Considering equal Split among all involved users,here is the summary of pay backs for the expenditure happened so far-");
                System.out.println(" ");
                for (Member member : currentExpenseStatusMap.keySet()) {
                    double value = currentExpenseStatusMap.get(member);
                    if (value >= 0)
                        System.out.println(member.getName() + " will receive amount: Rs." + value + " in total of all expenses");
                    else
                        System.out.println(member.getName() + " will give amount: Rs." + Math.abs(value) + " in total of all expenses");
                }
            }

        }

        return optimizedSettleUpStrategy.checkSettleUpSummary(currentExpenseStatusMap);

    }


}
