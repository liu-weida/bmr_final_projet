package org.rhw.bmr.project.service.impl;

import org.rhw.bmr.project.common.algo.KPMalgo;
import org.rhw.bmr.project.common.algo.egreplike.*;
import org.rhw.bmr.project.dto.req.TextInternalSearchByEgreplikeReqDTO;
import org.rhw.bmr.project.dto.req.TextInternalSearchByKMPReqDTO;
import org.rhw.bmr.project.service.TextInternalSearchService;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TextInternalSearchImpl implements TextInternalSearchService {


    @Override
    public String[] TextInternalSearchByKMP(TextInternalSearchByKMPReqDTO requestParam) {
        String url = requestParam.getURL(); // 获取文件路径
        String factor = requestParam.getWord(); // 获取匹配因子

        KPMalgo kpMalgo = new KPMalgo();
        List<String> matchingLines = new ArrayList<>();

        try {
            // 调用 KPM 算法匹配
            List<String> results = kpMalgo.KPM(factor, url);
            matchingLines.addAll(results);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed URL: " + url, e);
        }
        // 将匹配结果转换为字符串数组返回
        return matchingLines.toArray(new String[0]);
    }


    @Override
    public String[] TextInternalSearchByEgreplike(TextInternalSearchByEgreplikeReqDTO requestParam) {
        String url = requestParam.getURL(); // 获取文件路径
        String word = requestParam.getRegular(); // 获取正则表达式

        // 分析正则表达式
        RegEx regEx = new RegEx();
        RegExTree tree = regEx.toTree(word); // 将正则表达式转换为树

        Construction construction = new Construction(); // 初始化构造器
        NonDetFiniAuto ndfa = construction.convert(tree); // 使用 Thompson 构造算法生成 NFA
        DetFiniAuto dfa = construction.convertToDFA(ndfa); // 将 NFA 转换为 DFA
        DetFiniAuto minimizeDFA = construction.minimizeDFA(dfa); // 将 DFA 最小化

        List<String> matchingLines = new ArrayList<>(); // 用于存储匹配的行

        // 读取文件并匹配
        try (BufferedReader br = new BufferedReader(new FileReader(url))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // 使用最小化 DFA 匹配每一行
                if (matchesDFA(minimizeDFA, line)) {
                    matchingLines.add(lineNumber + ": " + line); // 如果匹配，存储结果
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将匹配结果转换为字符串数组返回
        return matchingLines.toArray(new String[0]);
    }



    @Override
    public long[] TextInternalSearchByKMPLong(TextInternalSearchByKMPReqDTO requestParam) {
        String url = requestParam.getURL(); // 获取文件路径
        String factor = requestParam.getWord(); // 获取匹配因子

        KPMalgo kpMalgo = new KPMalgo();
        List<Long> matchingLineNumbers;

        try {
            // 调用 KPM 算法匹配
            matchingLineNumbers = kpMalgo.KPMLong(factor, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed URL: " + url, e);
        }

        // 将匹配结果转换为 long 数组返回
        return matchingLineNumbers.stream().mapToLong(Long::longValue).toArray();
    }

    @Override
    public long[] TextInternalSearchByEgreplikeLong(TextInternalSearchByEgreplikeReqDTO requestParam) {
        String url = requestParam.getURL(); // 获取文件路径
        String regEx = requestParam.getRegular(); // 获取正则表达式

        RegEx regEx1 = new RegEx();
        RegExTree tree = regEx1.toTree(regEx); // 将正则表达式转换为树

        Construction construction = new Construction(); // 初始化构造器
        NonDetFiniAuto ndfa = construction.convert(tree); // 使用 Thompson 构造算法生成 NFA
        DetFiniAuto dfa = construction.convertToDFA(ndfa); // 将 NFA 转换为 DFA
        DetFiniAuto minimizeDFA = construction.minimizeDFA(dfa); // 将 DFA 最小化

        List<Long> matchingLineNumbers = new ArrayList<>();

        // 读取文件并匹配
        try (BufferedReader br = new BufferedReader(new FileReader(url))) {
            String line;
            long lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (matchesDFA(minimizeDFA, line)) {
                    matchingLineNumbers.add(lineNumber); // 存储匹配行号
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将匹配结果转换为 long 数组返回
        return matchingLineNumbers.stream().mapToLong(Long::longValue).toArray();
    }


    private static boolean matchesDFA(DetFiniAuto dfa, String line) {
        // 检查每一个子串是否被 DFA 接受
        for (int start = 0; start < line.length(); start++) {
            int currentState = dfa.startState;

            // 从当前起点逐字符匹配
            for (int i = start; i < line.length(); i++) {
                char c = line.charAt(i);

                // 获取当前状态的转移表
                Map<Character, Integer> transitions = dfa.transitions.get(currentState);
                if (transitions == null || !transitions.containsKey(c)) {
                    break; // 当前子串不匹配，检查下一个
                }

                // 转移到下一个状态
                currentState = transitions.get(c);

                // 如果当前状态是接受状态，返回匹配成功
                if (dfa.acceptStates.contains(currentState)) {
                    return true;
                }
            }
        }
        return false; // 没有子串匹配
    }
}
