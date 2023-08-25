package com.application.splitwise.executors;


import com.application.splitwise.Config.ScannerConfig;
import com.application.splitwise.Controllers.UserActionController;
import com.application.splitwise.Repository.GroupRepository;
import com.application.splitwise.models.SplitwiseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class CheckExpenseSummary implements Command{

    @Autowired
    private UserActionController userActionController;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public boolean matches(String command) {
        if(command.equalsIgnoreCase("CHECK_EXPENSE_STATUS"))
            return true;

        return false;
    }

    @Override
    public void execute() {
        List<SplitwiseGroup> allGroup = groupRepository.findAll();
        System.out.println("These are all the existing groups: ");
        allGroup.forEach(p -> System.out.println("id: " + p.getId() + " with name: " + p.getGroupName()));

        System.out.println("Enter the group id for which you wanna check expense summary ");
        Scanner scannerObject = ScannerConfig.getScannerObject();

        long groupId= scannerObject.nextLong();

        userActionController.checkExpenseStatus(groupId);

    }
}
