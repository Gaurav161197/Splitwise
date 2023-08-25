package com.application.splitwise;

import com.application.splitwise.Config.ScannerConfig;
import com.application.splitwise.Controllers.UserController;
import com.application.splitwise.executors.CommandLineExecutor;
import com.application.splitwise.executors.Command;
import com.application.splitwise.models.Member;
import com.application.splitwise.models.SplitwiseGroup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@Transactional
public class SplitwiseApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SplitwiseApplication.class, args);

        System.out.println("Welcome to Splitwise Command Line Application");

        Scanner scannerObject = ScannerConfig.getScannerObject();
        UserController userController = (UserController) run.getBean("userController");

        createGroupsAndUser(userController);

        CommandLineExecutor commandLineExecutor = (CommandLineExecutor) run.getBean("commandLineExecutor");
        Command commandBean1 = (Command) run.getBean("addExpense");
        Command commandBean2 = (Command) run.getBean("checkExpenseSummary");
        Command commandBean3 = (Command) run.getBean("settleUp");

        commandLineExecutor.addCommandProvider(commandBean1);
        commandLineExecutor.addCommandProvider(commandBean2);
        commandLineExecutor.addCommandProvider(commandBean3);


        List<Command> commandsList = commandLineExecutor.getCommandsList();
        System.out.println("Please choose from the following commands,type exit to EXIT...!");
        commandLineExecutor.viewAllCommands();
        String sc = scannerObject.next();
        while (!sc.equalsIgnoreCase("exit")) {
            for (Command command : commandsList) {
                if (command.matches(sc))
                    command.execute();
            }
            System.out.println("Please choose from the following commands: ");
            commandLineExecutor.viewAllCommands();
            sc = scannerObject.next();
        }
        System.out.println("Thanks for using Splitwise");


    }

    public static void createGroupsAndUser(UserController userController) {
        Member member1 = Member.getMemberBuilder().setName("Gaurav").setEmail("g@gmail.com").build();
        Member member2 = Member.getMemberBuilder().setName("Bhavik").setEmail("b@gmail.com").build();
        Member member3 = Member.getMemberBuilder().setName("Shruti").setEmail("s@gmail.com").build();
        Member member4 = Member.getMemberBuilder().setName("Pavan").setEmail("p@gmail.com").build();
        userController.registerMember(new ArrayList<>(Arrays.asList(member1, member2, member3, member4)));

        SplitwiseGroup goaExpenseGroup = SplitwiseGroup.getGroupBuilder().setGroupName("Goa Expense").setGroupMembers(new ArrayList<>(Arrays.asList(member1, member2, member3, member4))).setGroupAdmin(member1).build();
        userController.createGroup(goaExpenseGroup);
    }

}
