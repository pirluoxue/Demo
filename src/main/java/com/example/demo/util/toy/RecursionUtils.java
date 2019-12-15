package com.example.demo.util.toy;

import com.example.demo.model.entity.enums.LogCode;
import com.example.demo.model.entity.enums.RestCode;
import com.example.demo.model.entity.simple.MatchInfo;
import com.example.demo.util.io.IoStreamUtils;
import com.google.common.base.Strings;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author chen_bq
 * @description 递归工具类
 * @create: 2019/12/13 18:46
 **/
public class RecursionUtils {

    /**
     * @return java.util.List
     * @author: chen_bangqiang
     * @Date: 2019/12/15 21:12
     * @Description: 根据处理好的key值文件匹配dirPath路径下的所有对象
     * @Param [fileName, dirPath]
     */
    public static List listMatchEnunsByKeyListFile(String fileName, String dirPath) {
        if (Strings.isNullOrEmpty(fileName) || Strings.isNullOrEmpty(dirPath)) {
            return new ArrayList();
        }
        File file = new File(fileName);
        if (file == null) {
            return new ArrayList();
        }
        List<String> regexList = IoStreamUtils.getListByFileReadLine(file);
        return listMatchEnunsByEnumList(regexList, dirPath);
    }

    public static List listMatchEnunsByEnumList(List<String> enumList, String dirPath) {
        if (enumList == null || enumList.size() <= 0) {
            return new ArrayList();
        }
        if (Strings.isNullOrEmpty(dirPath)) {
            return new ArrayList();
        }
        File dirFile = new File(dirPath);
        if (dirFile == null) {
            return new ArrayList();
        }
        List<String> matchList = new ArrayList<>();
        IoStreamUtils.findPreciseForDir(enumList, dirFile, matchList);
        matchList = matchList.stream().distinct().collect(Collectors.toList());
        return matchList;
    }

    public static List<MatchInfo> listMatchEnunsByEnum(Class clazz, String dirPath) {
        if (!clazz.isEnum()) {
            return new ArrayList<>();
        }
        if (clazz.equals(LogCode.class)){
            List<String> enumList = new ArrayList<>();
            for (LogCode logCode : LogCode.getAll()) {
                enumList.add(logCode.name());
            }
            List<String> matchList = listMatchEnunsByEnumList(enumList, dirPath);
            return getMatchInfoByMatchList(matchList);
        }
        if (clazz.equals(RestCode.class)) {
            List<String> enumList = new ArrayList<>();
            for (RestCode restCode : RestCode.getAll()) {
                enumList.add(restCode.name());
            }
            List<String> matchList = listMatchEnunsByEnumList(enumList, dirPath);
            return getMatchInfoByMatchList(matchList);
        }
        System.out.println("不支持的枚举类型");
        return new ArrayList();
    }

    private static List<MatchInfo> getMatchInfoByMatchList(List<String> matchList) {
        List<MatchInfo> logCodes = new ArrayList<>();
        for (String segment : matchList) {
            String queryParam = foreachSearch(segment);
            MatchInfo info = new MatchInfo();
            info.setMatchResource(segment);
            try {
                Integer code = Integer.parseInt(queryParam);
                info.setMatchCode(code);
            } catch (Exception e) {
                info.setMatchParameter(queryParam);
            }
            logCodes.add(info);
        }
        return logCodes;
    }

    private static String foreachSearch(String segment) {
        Integer code = findLogCode(segment);
        if (code != null) {
            return String.valueOf(code);
        }
        return null;
    }

    private static Integer findLogCode(String segment) {
        LogCode logCode = LogCode.valueOf(segment);
        return logCode.getCode();
    }

}
