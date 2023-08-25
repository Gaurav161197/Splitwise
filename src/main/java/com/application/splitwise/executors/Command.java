package com.application.splitwise.executors;


import org.springframework.stereotype.Component;

@Component
public interface Command {
    public boolean matches(String command);
    public void execute();
}
