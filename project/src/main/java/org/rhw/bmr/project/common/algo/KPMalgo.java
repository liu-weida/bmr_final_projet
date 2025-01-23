package org.rhw.bmr.project.common.algo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class KPMalgo {
    public int[] computeCarryover(char[] pattern) {
        int len = pattern.length;
        int[] next = new int[len + 1];
        next[0] = -1;
        int i = 0, j = -1;

        // getLTS
        while (i < len) {
            if (j == -1 || pattern[i] == pattern[j]) {
                i++;
                j++;
                next[i] = j;
            } else {
                j = next[j];
            }
        }
        // LTS to carryover
        for (i = 1; i <= len; i++) {
            if (next[i] >= 0 && i < len && pattern[i] == pattern[next[i]]) {
                next[i] = next[next[i]];
            }
        }
        return next;
    }

    public List<String> KPM(String factor, String file) throws MalformedURLException {
        char[] factorChar = factor.toCharArray();
        int[] carryover = computeCarryover(factorChar);
        List<String> matchingLines = new ArrayList<>(); // 用于存储匹配的行

        // read from local file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                int i = 0;
                int j = 0;
                int textLen = line.length();
                int factorLen = factorChar.length;
                while (i < textLen) {
                    if (j == -1 || line.charAt(i) == factorChar[j]) {
                        i++;
                        j++;
                        if (j == factorLen) {
                            matchingLines.add(lineNumber + ": " + line); // 存储匹配的行
                            j = carryover[j];
                        }
                    } else {
                        j = carryover[j];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matchingLines;
    }

    public List<Long> KPMLong(String factor, String file) throws MalformedURLException {
        char[] factorChar = factor.toCharArray();
        int[] carryover = computeCarryover(factorChar);
        List<Long> matchingLineNumbers = new ArrayList<>(); // 用于存储匹配的行号

        // read from local file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            long lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                int i = 0;
                int j = 0;
                int textLen = line.length();
                int factorLen = factorChar.length;
                while (i < textLen) {
                    if (j == -1 || line.charAt(i) == factorChar[j]) {
                        i++;
                        j++;
                        if (j == factorLen) {
                            matchingLineNumbers.add(lineNumber); // 存储匹配的行号
                            j = carryover[j];
                        }
                    } else {
                        j = carryover[j];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matchingLineNumbers;
    }
}