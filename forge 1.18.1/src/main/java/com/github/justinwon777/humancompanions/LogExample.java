package com.github.justinwon777.humancompanions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogExample {
    private static final Logger logger = LogManager.getLogger("LogExample");
    public static void main(String[] args) {
        logger.info("Hello, World!");
    }
}