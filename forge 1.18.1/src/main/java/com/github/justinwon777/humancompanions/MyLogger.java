package com.github.justinwon777.humancompanions;

import net.minecraft.world.level.storage.LevelStorageSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.*;


public class MyLogger {


    private static MyLogger instance;

    private String filename = "C:/Users/Emma/IdeaProjects/HumanCompanions/forge 1.18.1/run/logs/customLogs/";
    private final String loggerName = "MyLogger";
    private static Logger logger;
    private FileHandler fh;
    private final int limit = 1024 * 10000; //10 MB
    private SimpleFormatter sf;

    private MyLogger() {
        logger = Logger.getLogger(loggerName);

        Date date = new Date();
        String timestamp2 = String.valueOf(date.getTime());

        filename = filename + timestamp2 + ".log";


        sf = new SimpleFormatter();
        try {
            fh = new FileHandler(filename, limit, 1, false);
            sf = new SimpleFormatter();
            fh.setFormatter(new SimpleFormatter() {
                private static final String format =
                        "[%1$tF %1$tT.%1$tL] [%2$-7s] %3$s %n";

                public synchronized String format(
                        LogRecord logRecord)
                {
                    return String.format(
                            format, new Date(logRecord.getMillis()),
                            logRecord.getLevel().getLocalizedName(),
                            logRecord.getMessage());
                }
            });

            logger.addHandler(fh);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
//        fh.close();
    }

    public static MyLogger getInstance() {
        if (instance == null) {
            instance = new MyLogger();
        }
        return instance;
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warning(String message) {
        logger.warning(message);
    }

    public void severe(String message) {
        logger.severe(message);
    }

}