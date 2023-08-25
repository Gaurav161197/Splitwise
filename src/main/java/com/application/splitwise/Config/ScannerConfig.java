package com.application.splitwise.Config;


import org.springframework.stereotype.Component;

import java.util.Scanner;


@Component
public class ScannerConfig {
    private static Scanner sc = null;

    private ScannerConfig() {
        // Private constructor to prevent instantiation
    }

    public static synchronized Scanner getScannerObject() {
        if (sc == null)
            return new Scanner(System.in);

        return sc;
    }
}
