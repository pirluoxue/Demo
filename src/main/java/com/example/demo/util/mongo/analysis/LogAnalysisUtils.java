package com.example.demo.util.mongo.analysis;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.common.JudgeUtils;
import com.example.demo.util.common.TypeChangeUtils;
import com.example.demo.util.io.IoStreamUtils;
import com.example.demo.util.mongo.analysis.entity.StatisticsLog;
import com.example.demo.util.test.simple.rabbitmq.helloworld.P;
import com.google.common.base.Strings;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author chen_bq
 * @description mongo慢查询分析工具
 * @create: 2019/10/16 10:27
 **/
public class LogAnalysisUtils {

    public static final String PUBLIC_REGEX_PRIFIX = "COMMAND";
//    public static final String AGGREGATE_REGEX_SUFFIX = ".*?op_query [0-9].{4,}ms";
//    public static final String QUERY_INSERT_REGEX_SUFFIX = ".*?} } } [0-9]{4,}ms";
    public static final String AGGREGATE_REGEX_SUFFIX = ".*?op_query [0-9]+ms";
    public static final String QUERY_INSERT_REGEX_SUFFIX = ".*?} } } [0-9]+ms";
    public static final String DATE_REGEX_SUFFIX = ".{13}";
    public static final String CHARACTERISTIC_REGEX = "(\\$.*?(?=:|\"))|((?<= )[a-z|A-Z|_]{2,}(?=:))";
    public static final String COMMAND_JSON_REGEX = "((update: |query: |aggregate |count ).*?}(?=( [a-z])))|(ninserted:[0-9]+)";
    public static final String COMMAND_TYPE_DESCRIPTION_REGEX = "I COMMAND  .*?(?=(: \\{|:[0-9]+| \\{))";
    public static final String COMMAND_TYPE_DESCRIPTION_REPLACE_DIRTY_REGEX = "I COMMAND.*?\\] ";
    public static final String COMMAND_NAME_REGEX = "(?<=(] )).*?:( |[0-9]+)([a-z|A-Z]*)";
    public static final String LOCKS_REGEX = "locks.*?} } }";
    public static final String LOCKS_ATTRIBUTE_NAME_REGEX = "([a-z|A-Z]+(?=:))";
    public static final String LOCKS_ATTRIBUTE_REPLACE = "\"$1\"";
    public static final String COLLECTION_NAME_REGEX = "(?<=(macc2\\.)).*?(?=( [a-z|A-Z])+)";
    public static final String OPERATION_TIME_REGEX = "[0-9]+(?=ms)";
    public static final String NUM_YIELDS_REGEX = "(?<=(numYields:))[0-9]+";
    public static final String RESULT_REGEX = "(ninserted|ndeleted|reslen|nreturned|nModified):[0-9]+";
    public static final String RESLEN_REGEX = "(?<=reslen:)[0-9]+";

    /**
     * @Author chen_bq
     * @Description 收集慢查询日志
     * @Date 2019/10/18 14:26
     * @Param [file, formatDateTime]
     * @return java.util.List<com.example.demo.util.mongo.analysis.entity.StatisticsLog>
     */
    public static List<StatisticsLog> collectMongoDBLogForSlow(File file, String formatDateTime) {
        if (!file.exists()) {
            return null;
        }
        String log = IoStreamUtils.getStringByFile(file);
        List<StatisticsLog> aggregateList = regexAnalysis(log, formatDateTime, AGGREGATE_REGEX_SUFFIX);
        List<StatisticsLog> queryInsertList = regexAnalysis(log, formatDateTime, QUERY_INSERT_REGEX_SUFFIX);
        List<StatisticsLog> sortList = TypeChangeUtils.mergeList(aggregateList, queryInsertList).stream().sorted((p1, p2) -> (p1.getLogDateTime().compareTo(p2.getLogDateTime())))
            .collect(Collectors.toList());
        return sortList;
    }

    /**
     * @Author chen_bq
     * @Description 分析日志
     * @Date 2019/10/18 14:24
     * @Param [logs]
     * @return void
     */
    public static void analysisMongoLogByMongoLog(List<StatisticsLog> logs) {
        if (logs == null || logs.size() <= 0) {
            return;
        }
        supplyCharacteristic(logs);
        supplyLocks(logs);
        supplyCollectionName(logs);
        supplyCommandTypeDescrition(logs);
        supplyNumYields(logs);
        supplyOperationTime(logs);
        supplyResult(logs);
        supplyReslen(logs);
        supplyUseinfomation(logs);
    }

    /**
     * @Author chen_bq
     * @Description 收集使用信息
     * @Date 2019/10/18 14:25
     * @Param [logs]
     * @return java.util.List<com.example.demo.util.mongo.analysis.entity.StatisticsLog>
     */
    public static List<StatisticsLog> collectUseinfomation(List<StatisticsLog> logs) {
        if (logs == null || logs.size() <= 0){
            return new ArrayList<>();
        }
        analysisMongoLogByMongoLog(logs);
        clearNPE(logs);
        int index = 0;
        List<StatisticsLog> checkOutLogs = new ArrayList<>();
        while (logs.size() > 0) {
            StatisticsLog uniqueLog = logs.get(index);
            logs.remove(index);
            Iterator it = logs.iterator();
            List<StatisticsLog> comparedLogs = new ArrayList<>();
            while (it.hasNext()) {
                StatisticsLog comparedLog = (StatisticsLog)it.next();
                if (uniqueLog.getCollectionName().compareTo(comparedLog.getCollectionName()) != 0) {
                    continue;
                }
                if (!JudgeUtils.listEquals(uniqueLog.getCharacteristic(), comparedLog.getCharacteristic())) {
                    continue;
                }
                // 相同日志入列
                comparedLogs.add(comparedLog);
                it.remove();
            }
            // 唯一日志入列
            comparedLogs.add(uniqueLog);
            // 合并相同日志信息
            uniqueLog = supplyUseinfomation(comparedLogs);
            checkOutLogs.add(uniqueLog);
        }
        return checkOutLogs;
    }

    private static void clearNPE(List<StatisticsLog> logs){
        Iterator<StatisticsLog> it = logs.iterator();
        while (it.hasNext()){
            StatisticsLog log = it.next();
            if (Strings.isNullOrEmpty(log.getCollectionName())){
                System.out.println(log.getCommandTypeDescrition() + log);
                it.remove();
            }
        }
    }

    /**
     * @return void
     * @Author chen_bq
     * @Description 比较对象填充使用信息
     * @Date 2019/10/18 9:42
     * @Param [checkOutLog, resourceLog]
     */
    private static StatisticsLog supplyUseinfomation(List<StatisticsLog> checkOutLogs) {
        if (checkOutLogs == null || checkOutLogs.size() <= 0) {
            return new StatisticsLog();
        }
        int number = checkOutLogs.size();
        StatisticsLog uniqueLog = checkOutLogs.get(0);
        uniqueLog.setUseTimes(number);
        long operationTime = 0;
        int maxOperationTime = checkOutLogs.get(0).getOperationTime();
        long numYields = 0;
        int maxNumYields = checkOutLogs.get(0).getNumYields();
        long reslen = 0;
        int maxReslen = checkOutLogs.get(0).getReslen();
        for (StatisticsLog log : checkOutLogs) {
            operationTime += log.getOperationTime();
            maxOperationTime = Integer.max(maxOperationTime, log.getOperationTime());
            reslen += log.getReslen();
            maxNumYields = Integer.max(maxNumYields, log.getNumYields());
            numYields += log.getNumYields();
            maxReslen = Integer.max(maxReslen, log.getReslen());
        }
        uniqueLog.setAvgOperationTime((int)operationTime / number);
        uniqueLog.setAvgReslen((int)reslen / number);
        uniqueLog.setAvgNumYields((int)numYields / number);
        uniqueLog.setMaxOperationTime(maxOperationTime);
        uniqueLog.setMaxNumYields(maxNumYields);
        uniqueLog.setMaxReslen(maxReslen);
        return uniqueLog;
    }

    private static void supplyResult(List<StatisticsLog> logs){
        Pattern pattern = Pattern.compile(RESULT_REGEX);
        for (StatisticsLog logEntity: logs){
            Matcher matcher = pattern.matcher(logEntity.getLogMessage());
            StringBuilder resultt = new StringBuilder();
            while (matcher.find()){
                resultt.append(matcher.group() + " ");
            }
            logEntity.setResult(resultt.toString());
        }
    }

    private static void supplyReslen(List<StatisticsLog> logs){
        Pattern pattern = Pattern.compile(RESLEN_REGEX);
        for (StatisticsLog logEntity: logs){
            Matcher matcher = pattern.matcher(logEntity.getLogMessage());
            if (matcher.find()){
                logEntity.setReslen(Integer.parseInt(matcher.group()));
            }
        }
    }

    private static void supplyOperationTime(List<StatisticsLog> logs){
        Pattern pattern = Pattern.compile(OPERATION_TIME_REGEX);
        for (StatisticsLog logEntity: logs){
            Matcher matcher = pattern.matcher(logEntity.getLogMessage());
            if (matcher.find()){
                logEntity.setOperationTime(Integer.parseInt(matcher.group()));
            }
        }
    }

    private static void supplyNumYields(List<StatisticsLog> logs){
        Pattern pattern = Pattern.compile(NUM_YIELDS_REGEX);
        for (StatisticsLog logEntity: logs){
            Matcher matcher = pattern.matcher(logEntity.getLogMessage());
            if (matcher.find()){
                logEntity.setNumYields(Integer.parseInt(matcher.group()));
            }
        }
    }

    private static void supplyCommandTypeDescrition(List<StatisticsLog> logs){
        Pattern pattern = Pattern.compile(COMMAND_TYPE_DESCRIPTION_REGEX);
        Pattern replacePattern = Pattern.compile(COMMAND_TYPE_DESCRIPTION_REPLACE_DIRTY_REGEX);
        for (StatisticsLog logEntity: logs){
            Matcher matcher = pattern.matcher(logEntity.getLogMessage());
            if (matcher.find()){
                String originDescrition = matcher.group();
                Matcher replaceMatcher = replacePattern.matcher(originDescrition);
                if (replaceMatcher.find()){
                    String replaceString = replaceMatcher.group();
                    originDescrition = originDescrition.replace(replaceString, "");
                    logEntity.setCommandTypeDescrition(originDescrition);
                }
            }
        }
    }
    private static void supplyCollectionName(List<StatisticsLog> logs){
        Pattern pattern = Pattern.compile(COLLECTION_NAME_REGEX);
        for (StatisticsLog logEntity: logs){
            Matcher matcher = pattern.matcher(logEntity.getLogMessage());
            if (matcher.find()){
                logEntity.setCollectionName(matcher.group());
            }
        }
    }

    /**
     * @Author chen_bq
     * @Description 填充锁相关信息
     * @Date 2019/10/16 16:53
     * @Param [logs]
     * @return void
     */
    private static void supplyLocks(List<StatisticsLog> logs){
        Pattern locksPattern = Pattern.compile(LOCKS_REGEX);
        for (int i = 0; i < logs.size(); i++) {
            Matcher matcher = locksPattern.matcher(logs.get(i).getLogMessage());
            if (matcher.find()){
                String locks = buildLocksJson(matcher.group());
                JSONObject jsonObject = JSON.parseObject(locks);
                logs.get(i).setLocks(jsonObject.getInnerMap());
            }
        }
    }

    /**
     * @Author chen_bq
     * @Description 构建json类型的locks信息
     * @Date 2019/10/16 16:58
     * @Param [locksString]
     * @return void
     */
    private static String buildLocksJson(String locksString){
        if (Strings.isNullOrEmpty(locksString)){
            return null;
        }
        locksString = "{ " + locksString + " }";
        // 填充双引号
        locksString = locksString.replaceAll(LOCKS_ATTRIBUTE_NAME_REGEX, LOCKS_ATTRIBUTE_REPLACE);
        return locksString;
    }
    
    /**
     * @Author chen_bq
     * @Description 填充特征信息list
     * @Date 2019/10/16 16:53
     * @Param [logs]
     * @return void
     */
    private static void supplyCharacteristic(List<StatisticsLog> logs){
        Pattern commandPattern = Pattern.compile(COMMAND_JSON_REGEX);
        Pattern characteristic = Pattern.compile(CHARACTERISTIC_REGEX);
        for (int i = 0; i < logs.size(); i++) {
            Matcher commandMatcher = commandPattern.matcher(logs.get(i).getLogMessage());
            if (commandMatcher.find()){
                Matcher matcher = characteristic.matcher(commandMatcher.group());
                List characteristicList = new ArrayList();
                while (matcher.find()){
                    characteristicList.add(matcher.group());
                }
                logs.get(i).setCharacteristic(characteristicList);
            }
        }
    }

    private static List<StatisticsLog> regexAnalysis(String document, String formatDateTime, String regex) {
        String fullRegex = formatDateTime + regex;
        Pattern pattern = Pattern.compile(fullRegex);
        Matcher matcher = pattern.matcher(document);
        List<StatisticsLog> matchList = new ArrayList();
        while (matcher.find()) {
            String log = matcher.group();
            String logDateTime = getLogDateTime(log, formatDateTime);
            StatisticsLog logEntity = new StatisticsLog();
            logEntity.setLogDateTime(logDateTime);
            logEntity.setLogMessage(log);
            matchList.add(logEntity);
        }
        return matchList;
    }

    private static String getLogDateTime(String log, String logDate) {
        Pattern pattern = Pattern.compile(logDate + DATE_REGEX_SUFFIX);
        Matcher matcher = pattern.matcher(log);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    /**
     * @Author chen_bq
     * @Description 根据list获取输出日志List
     * @Date 2019/10/18 14:40
     * @Param [statisticsLogs]
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getLogsForWrite(List<StatisticsLog> statisticsLogs) {
        List<String> list = new ArrayList<>();
        for (StatisticsLog log : statisticsLogs) {
            list.add(log.getLogMessage());
        }
        return list;
    }

    /**
     * @Author chen_bq
     * @Description 根据统计列表输出统计报表 xlsx格式
     * @Date 2019/10/18 16:47
     * @Param [statisticsLogs]
     * @return java.util.List<java.lang.String>
     */
    public static List<List<String>> getStatisticsReportForWriteXlsx(List<StatisticsLog> statisticsLogs) {
        List<List<String>> list = new ArrayList<>();
        list.add(initStatisticsReportForXlsx());
        for (StatisticsLog log : statisticsLogs) {
            list.add(buildStatisticsReportLineForXlsx(log));
        }
        return list;
    }

    /**
     * @Author chen_bq
     * @Description 根据统计列表输出统计报表 txt格式
     * @Date 2019/10/18 14:41
     * @Param [statisticsLogs]
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getStatisticsReportForWriteTXT(List<StatisticsLog> statisticsLogs) {
        List<String> list = new ArrayList<>();
        list.add(initStatisticsReport());
        for (StatisticsLog log : statisticsLogs) {
            list.add(buildStatisticsReportLine(log));
        }
        return list;
    }

    private static String initStatisticsReport(){
        StringBuilder logLine = new StringBuilder();
        String split = "    ";
        logLine.append("CollectionName" + split);
        logLine.append("CommandTypeDescrition" + split);
        logLine.append("MaxOperationTime" + split);
        logLine.append("AvgOperationTime" + split);
        logLine.append("MaxReslen" + split);
        logLine.append("UseTimes" + split);
        logLine.append("Example Result" + split);
        logLine.append("LogDateTime" + split);
        return logLine.toString();
    }

    private static List<String> initStatisticsReportForXlsx(){
        List<String> logLine = new ArrayList<>();
        logLine.add("CollectionName");
        logLine.add("CommandTypeDescrition");
        logLine.add("MaxOperationTime");
        logLine.add("AvgOperationTime");
        logLine.add("MaxReslen");
        logLine.add("AvgReslen");
        logLine.add("UseTimes");
        logLine.add("Example Result");
        logLine.add("Example Command");
        return logLine;
    }

    private static String buildStatisticsReportLine(StatisticsLog log){
        StringBuilder logLine = new StringBuilder();
        String split = "    ";
        String ms = "ms";
        String bytes = "byte";
        logLine.append(log.getCollectionName() + split);
        logLine.append(log.getCommandTypeDescrition() + split);
        logLine.append(log.getMaxOperationTime() + split + ms);
        logLine.append(log.getAvgOperationTime() + split + bytes);
        logLine.append(log.getMaxReslen() + split + bytes);
        logLine.append(log.getUseTimes() + split);
        logLine.append(log.getResult() + split);
        logLine.append(log.getLogDateTime() + split);
        return logLine.toString();
    }

    private static List<String> buildStatisticsReportLineForXlsx(StatisticsLog log){
        List<String> logLine = new ArrayList<>();
        String ms = " ms";
        String bytes = " byte";
        logLine.add(log.getCollectionName());
        logLine.add(log.getCommandTypeDescrition());
        logLine.add(log.getMaxOperationTime() + ms);
        logLine.add(log.getAvgOperationTime() + ms);
        logLine.add(log.getMaxReslen() + bytes);
        logLine.add(log.getAvgReslen() + bytes);
        logLine.add(log.getUseTimes() + "");
        logLine.add(log.getResult());
        logLine.add(log.getLogMessage());
        return logLine;
    }

    public static void main(String[] args) {
        String message1 = "message1";
        String formatDateTime1 = "2019-10-17";
        String message2 = "message2";
        String formatDateTime2 = "2019-10-17";
        String message3 = "message3";
        String formatDateTime3 = "2019-10-18";
        List<StatisticsLog> list = new ArrayList();
        StatisticsLog logEntity1 = new StatisticsLog();
        logEntity1.setLogMessage(message1);
        logEntity1.setLogDateTime(formatDateTime1);
        list.add(logEntity1);
        StatisticsLog logEntity2 = new StatisticsLog();
        logEntity2.setLogMessage(message2);
        logEntity2.setLogDateTime(formatDateTime2);
        list.add(logEntity2);
        StatisticsLog logEntity3 = new StatisticsLog();
        logEntity3.setLogMessage(message3);
        logEntity3.setLogDateTime(formatDateTime3);
        list.add(logEntity3);
        list = list.stream().sorted((p1, p2) -> {
            if (JudgeUtils.listEquals(p1.getCharacteristic(), p2.getCharacteristic())){
                return -1;
            }
            return 1;
        }).collect(Collectors.toList());
        System.out.println(list);
    }


/*    public static void main(String[] args) {
//        String url = "E:\\work\\工作文档\\mongo.out";
//        String outFileName = "E:\\work\\工作文档\\Analysis_mongo.txt";
        String url = "E:\\work\\工作文档\\test.txt";
        String outFileName = "E:\\work\\工作文档\\outText.txt";
        File file = new File(url);
        System.out.println(file.getName());
        System.out.println(file.getAbsolutePath());
        String formatDate = "2019-10-15";
        List<StatisticsLog> mongoLogEntities = LogAnalysisUtils.collectMongoDBLogForSlow(file, formatDate);
        String log = TypeChangeUtils.listToLineFeedString(LogAnalysisUtils.getLogByStatisticsLogList(mongoLogEntities));
        IoStreamUtils.writeTextIntoFile(log, outFileName);
    }*/

}
