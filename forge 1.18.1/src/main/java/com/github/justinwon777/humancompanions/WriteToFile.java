package com.github.justinwon777.humancompanions;

import com.github.justinwon777.humancompanions.entity.AbstractHumanCompanionEntity;
import com.github.justinwon777.humancompanions.entity.CompanionData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class WriteToFile {
    public static void main() {
        Instant instant = Instant.now();
        String output = instant.toString();
        try {
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("C:\\Users\\Emma\\testlogger.txt"));
            if (CompanionData.questBegin) {
                myWriter.write(output + " quest_begin");
                myWriter.flush();
                System.out.println("printed properly");
            }
            else
                myWriter.write("didn't work");
                myWriter.flush();
                System.out.println("did not print properly");
//            myWriter.write(String.valueOf(CompanionData.questBegin));
//            myWriter.flush();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}