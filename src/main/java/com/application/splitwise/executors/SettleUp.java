package com.application.splitwise.executors;


import com.application.splitwise.DTOs.AddExpenseRequestResource;
import com.application.splitwise.Config.ScannerConfig;
import com.application.splitwise.Controllers.UserActionController;
import com.application.splitwise.Repository.GroupRepository;
import com.application.splitwise.models.Member;
import com.application.splitwise.models.SettleUpSummaryResource;
import com.application.splitwise.models.SplitwiseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SettleUp implements Command {

    @Autowired
    private UserActionController userActionController;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public boolean matches(String command) {
        if (command.equalsIgnoreCase("settle_up"))
            return true;
        return false;
    }

    @Override
    public void execute() {
        List<SplitwiseGroup> allGroup = groupRepository.findAll();
        System.out.println("These are all the existing group: ");
        allGroup.forEach(p -> System.out.println("id: " + p.getId() + " with name: " + p.getGroupName()));

        System.out.println("Enter the group id for which you wanna check expense summary ");
        Scanner scannerObject = ScannerConfig.getScannerObject();

        long groupId = scannerObject.nextLong();

        List<SettleUpSummaryResource> settleUpSummaryResources = userActionController.checkExpenseStatus(groupId);

        System.out.println("Which expense summary id you want to settle up..?");
        int expenseSummaryId = scannerObject.nextInt();
        SettleUpSummaryResource settleUpSummaryResource = settleUpSummaryResources.stream().filter(r -> expenseSummaryId == r.getId()).findFirst().get();
        AddExpenseRequestResource addExpenseRequestResource = new AddExpenseRequestResource();
        List<Member> userInvolved = new ArrayList<>(Arrays.asList(settleUpSummaryResource.getPaidTo()));
        Map<Member, Double> amountPaidByUser = new HashMap<>();
        amountPaidByUser.put(settleUpSummaryResource.getPaidBy(), settleUpSummaryResource.getAmount());
        addExpenseRequestResource.setGroupId(groupId);
        addExpenseRequestResource.setAmount(settleUpSummaryResource.getAmount());
        addExpenseRequestResource.setUsersInvolved(userInvolved);
        addExpenseRequestResource.setAmountPaidByUser(amountPaidByUser);
        userActionController.addExpense(addExpenseRequestResource);

    }
}
