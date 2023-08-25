package com.application.splitwise.executors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CommandLineExecutor {

    private List<Command> commandsList = new ArrayList<>();

    public void addCommandProvider(Command command) {
        commandsList.add(command);
    }

    public void viewAllCommands() {
        Arrays.stream(CommandValues.values()).forEach(System.out::println);
    }

    public void executeCommand(String input) {
        for (Command command : commandsList) {
            if (command.matches(input))
                command.execute();
        }

    }

    public List<Command> getCommandsList() {
        return this.commandsList;
    }

}

