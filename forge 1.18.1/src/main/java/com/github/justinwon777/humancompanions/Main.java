package com.github.justinwon777.humancompanions;

import java.util.logging.Level;
import java.util.logging.Logger;

class MyLevel extends Level {
    public static final Level DISASTER = new MyLevel("DISASTER", Level.SEVERE.intValue() + 1);

    public MyLevel(String name, int value) {
        super(name, value);
    }
}

public class Main {
    public static void main(String[] argv) throws Exception {
        Logger logger = Logger.getLogger("com.mycompany");
        logger.log(MyLevel.DISASTER, "my disaster message");

        Level disaster = Level.parse("DISASTER");
        logger.log(disaster, "my disaster message");
    }
}