package com.github.justinwon777.humancompanions;

import com.github.justinwon777.humancompanions.entity.CompanionData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {
    public static void main() {
        try {
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("C:\\Users\\Emma\\testlogger.txt"));
            myWriter.write(String.valueOf(CompanionData.questBegin));
            myWriter.flush();
            myWriter.write(String.valueOf(CompanionData.questBegin));
            myWriter.flush();
            System.out.println("printed something");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}