package com.example.demo.util.toy;

import com.example.demo.components.annotation.OperationLog;
import com.example.demo.model.entity.enums.*;
import com.example.demo.model.entity.simple.MatchInfo;
import com.example.demo.util.ConnectionUtil;
import com.example.demo.util.io.IoStreamUtils;
import org.junit.Test;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RecursionUtilsTest {

    @Test
    public void listMatchEnunsByEnumTest() throws SQLException {
        /* 选择匹配路径和匹配枚举对象 */
        String path = "E:\\workplace\\China\\server\\macc-maintenance\\src\\main\\java\\com\\ruijie\\cloud\\macc";
        List<MatchInfo> list = RecursionUtils.listMatchEnunsByEnum(RestCode.class, path);
        /* 选择匹配路径和匹配枚举对象 */

        list = list.stream().distinct().collect(Collectors.toList());
        Set<Integer> codeSet = new HashSet<>();
        for (MatchInfo matchInfo : list) {
            codeSet.add(matchInfo.getMatchCode());
        }
        List<String> msgList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        List<String> mergeList = new ArrayList<>();
        ConnectionUtil connectionUtil = getConnectionUtil();
        for (Integer code : codeSet) {
            if (code == null) {
                continue;
            }
            String sql = getSQLForRestCode(code);
//            String sql = getSQLForLogCode(code);
            PreparedStatement pstmt = connectionUtil.getConn().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                for (MatchInfo matchInfo : list) {
                    if (matchInfo.getMatchCode().compareTo(code) == 0) {
                        msgList.add(matchInfo.getMatchMsg());
                        titleList.add(matchInfo.getMatchTitle());
//                        mergeList.add(code + "  " + matchInfo.getMatchMsg() + "  " + matchInfo.getMatchTitle());
                        mergeList.add(code + "  " + matchInfo.getMatchMsg());
                        break;
                    }
                }
            }
        }
        for (String info : mergeList) {
            System.out.println(info);
        }
    }

    @Test
    public void enumsMapperMySQL() throws SQLException {
        List<Integer> keys = new ArrayList<>();
        for (WarnCode warnCode : WarnCode.values()) {
            keys.add(warnCode.getCode());
        }
        //        List<Integer> keys = new ArrayList<>();
//        for (RestCode restCode : RestCode.values()) {
//            keys.add(restCode.getCode());
//        }
        List<Integer> lostKey = new ArrayList();
        ConnectionUtil connectionUtil = getConnectionUtil();
        for (Integer key : keys) {
            String sql = getSQLForWarnCode(key);
//            String sql = getSQLForRestCode(key);
            PreparedStatement pstmt = connectionUtil.getConn().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                lostKey.add(key);
            }
        }
        for (Integer key : lostKey) {
            String chineseKey = WarnCode.getByCode(key).getTitle() + "   " + WarnCode.getByCode(key).getMsg();
            System.out.println(key + "   " + chineseKey);
        }
    }

    @Test
    public void createInsert() {
        String url = "E:\\work\\国际化文档\\warnCode20200309.txt";
        File file = new File(url);
        if (file == null) {
            return;
        }
        List<String> list = IoStreamUtils.getListByFileReadLine(file);
        for (String line : list) {
            String codeRegex = "[0-9]+";
//            String resKeyRegex = "[0-9a-z].+  ";
            String resValueRegex = "[A-Z].+";
//            String paramRegex = "([A-Z])+.*";
//            String paramRegex = "(\\{|[A-Z])+.*";
            String code = IoStreamUtils.regexMatchDocument(line, codeRegex);
            String param = IoStreamUtils.regexMatchDocument(line, resValueRegex);
            String[] params = param.split("   ");
            if (param != null) {
//                System.out.println(buildSQLForInsertLogCode(code, params[0], params[1]));
                code = code.trim();
//                param = param.trim();
                System.out.println(buildSQLForInsertWarnCode(code.trim(), params[0].trim(), params[1].trim()));
            } else {
                System.out.println(code + " cannot paramters");
            }
        }


    }

    private String buildSQLForInsertWarnCode(String code, String title, String msg){
        return "insert ignore intl_warn_msg_en values (" + code + ", '" + title + "', '" + msg + "');";
    }

    private String buildSQLForInsertOperationCode(String resKey, String resValue){
        return "insert ignore macc_resources_en values ('" + resKey + "', '" + resValue + "');";
    }

    private String buildSQLForInsertRestCode(String code, String msg){
        return "insert ignore intl_api_msg_en values (" + code + ", '" + msg + "');";
    }

    private String buildSQLForInsertLogCode(String code, String title, String msg) {
        return "insert ignore intl_log_msg_en values (" + code + ", '" + title + "', '" + msg + "');";
    }

    private String getSQLForLogCode(int code) {
        return "select * from intl_log_msg_en where code = " + code;
    }

    private String getSQLForRestCode(int code) {
        return "select * from intl_api_msg_en where code = " + code;
    }

    private String getSQLForWarnType(String key) {
        return "select * from macc_resources_en where res_key = '" + key + "'";
    }

    private String getSQLForWarnCode(int key) {
        return "select * from intl_warn_msg_en where code = " + key;
    }

    private ConnectionUtil getConnectionUtil() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://172.29.45.158:3306/macc2?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull";
        String username = "macc";
        String password = "P@ssw0rd@macc2015";
        ConnectionUtil connectionUtil = new ConnectionUtil(driver, url, username, password);
        return connectionUtil;
    }

    @Test
    public void compareText() {
        String text1 = "C:\\Users\\bangqiang chen\\Desktop\\compare\\V2.6国际化-第一版.txt";
        String compare = "C:\\Users\\bangqiang chen\\Desktop\\compare\\new.txt";
        List<String> list1 = IoStreamUtils.getListByFileReadLine(new File(text1));
        List<String> compareList = IoStreamUtils.getListByFileReadLine(new File(compare));
        Iterator<String> it = compareList.iterator();
        while (it.hasNext()) {
            String compareStr = it.next();
            for (String resource : list1) {
                if (resource.indexOf(compareStr) >= 0) {
                    it.remove();
                    break;
                }
            }
        }
        for (String str : compareList) {
            System.out.println(str);
        }
    }

    @Test
    public void test1() {
        String text1 = "E:\\workplace\\domestic\\server\\macc-boot\\src\\main\\resources\\db\\macc\\repeatable\\intl\\R__t_intl_conf_item.sql";
        String compare = "C:\\Users\\bangqiang chen\\Desktop\\配置词条.txt";
        String decorate = "C:\\Users\\bangqiang chen\\Desktop\\配置词条 - en.txt";
        List<String> list1 = IoStreamUtils.getListByFileReadLine(new File(text1));
        List<String> compareList = IoStreamUtils.getListByFileReadLine(new File(compare));
        List<String> decorateList = IoStreamUtils.getListByFileReadLine(new File(decorate));
        Iterator<String> it = compareList.iterator();
        List<String> codeList = new LinkedList<>();
        String codeRegex = "(?<=(VALUES \\(')).*?(?=')";
        Pattern pattern = Pattern.compile(codeRegex);
        while (it.hasNext()) {
            String compareStr = it.next();
            for (String resource : list1) {
                if (resource.indexOf(compareStr) >= 0) {
                    Matcher matcher = pattern.matcher(resource);
                    if (matcher.find()) {
                        String code = matcher.group();
                        codeList.add(code);
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < codeList.size(); i++) {
            String sql = "INSERT ignore INTO `intl_conf_item_en` (`conf_item`, `msg`) VALUES ('" + codeList.get(i) + "', '" + decorateList.get(i) + "');";
            System.out.println(sql);
        }
    }

    @Test
    public void test2() throws SQLException {
        String resource = "C:\\Users\\bangqiang chen\\Desktop\\resource_en.txt";
        List<String> list = IoStreamUtils.getListByFileReadLine(new File(resource));
        if (list == null || list.size() <= 0) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        ConnectionUtil connectionUtil = getConnectionUtil();
        for (String resKey : list) {
            String sql = "select 1 from macc_resources_en where res_key = '" + resKey + "'";
            PreparedStatement pstmt = connectionUtil.getConn().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                sql = "select res_value from macc_resources_cn where res_key = '" + resKey + "'";
                PreparedStatement preparedStatement = connectionUtil.getConn().prepareStatement(sql);
                ResultSet rs2 = pstmt.executeQuery();
                if (rs2.next()) {
                    map.put(resKey, rs2.getString("res_value"));
                } else {
                    // 检索不到中文
                    map.put(resKey, MaccResourcesKey.getDesc(resKey));
                }
            }
        }
        for (Map.Entry entry : map.entrySet()) {
            System.out.println(entry.getKey() + "   " + entry.getValue());
        }
    }
}