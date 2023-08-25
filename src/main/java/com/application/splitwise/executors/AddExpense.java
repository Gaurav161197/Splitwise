package com.application.splitwise.executors;


import com.application.splitwise.Config.ScannerConfig;
import com.application.splitwise.Controllers.UserActionController;
import com.application.splitwise.Repository.GroupRepository;
import com.application.splitwise.Services.ExpenseActionService;
import com.application.splitwise.models.Member;
import com.application.splitwise.models.SplitwiseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AddExpense implements Command {

    @Autowired
    private GroupRepository groupRepository;


    @Autowired
    private ExpenseActionService expenseActionService;


    @Override
    public boolean matches(String command) {
        if (command.equalsIgnoreCase("add_expense"))
            return true;
        return false;
    }

    @Override
    public void execute() {
        System.out.println("For which group id you want to add expense?Please provide the group id. These are the all the groups that exists:");
        List<SplitwiseGroup> allGroup = groupRepository.findAll();
        allGroup.forEach(p -> System.out.println("id: " + p.getId() + " with name: " + p.getGroupName()));


        Scanner scannerObject = ScannerConfig.getScannerObject();
        long group_id = scannerObject.nextLong();

        SplitwiseGroup splitwiseGroup = groupRepository.findById(group_id).orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + group_id));

        List<Member> groupMembers = splitwiseGroup.getGroupMembers();
        System.out.println("These are the current members of the group " + splitwiseGroup.getGroupName());
        groupMembers.forEach(m -> System.out.println("member id: " + m.getId() + " name: " + m.getName()));
        System.out.println("What was the total amount paid..?");
        double totalAmount = scannerObject.nextDouble();
        System.out.println("Who all where involved in the transaction?, Please provide the associated ids OR Enter 0 to exit..!");
        List<Member> memberList = new ArrayList<>();
        long memberId = scannerObject.nextLong();
        List<Long> allMembersInvolved = new ArrayList<>();
        while (memberId != 0) {
            allMembersInvolved.add(memberId);
            memberId = scannerObject.nextLong();
        }


        List<Member> memberInvolvedInTransactionList = groupMembers.stream()
                .filter(member -> allMembersInvolved.contains(member.getId()))
                .peek(member -> {
                    if (!groupMembers.contains(member)) {
                        throw new IllegalArgumentException("member ID: " + member.getId() + " is not part of group : " + splitwiseGroup.getGroupName());
                    }
                })
                .collect(Collectors.toList());

        Map<Member, Double> amountPaidMap = new HashMap<>();
        double inputAmountSum = 0;
        for (Member member : memberInvolvedInTransactionList) {
            System.out.println("Enter amount paid By : " + member.getName());
            double amountPaid = scannerObject.nextDouble();
            inputAmountSum += amountPaid;
            amountPaidMap.put(member, amountPaid);
        }

        if (inputAmountSum != totalAmount)
            throw new IllegalArgumentException("Total amount does not sums up..!");

        expenseActionService.addExpense(group_id, totalAmount, amountPaidMap, memberInvolvedInTransactionList);


    }
}
