package com.example.demo.util;

import com.example.demo.model.entity.enums.LogCode;
import com.example.demo.model.entity.jooq.tables.records.UserRecord;
import com.example.demo.util.io.IoStreamUtils;
import com.google.common.base.Strings;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DataResourceUtilTest {

    @Test
    public void searchTest() throws IOException, SQLException {
        String findPath = "C:\\Users\\bangqiang chen\\Desktop\\asd.txt";
        String modelPath = "E:\\workplace\\domestic\\server\\macc-common-entity\\src\\main\\java\\com\\ruijie\\cloud\\core\\enums\\LogCode.java";
        List<LogCode> logCodes = matchCodeFile(findPath, modelPath);
        if (logCodes == null || logCodes.size() <= 0) {
            System.out.println("没有匹配到");
        }
        List<String> targetList = new ArrayList<>();

        ConnectionUtil connectionUtil = getConnectionUtil();
        for (LogCode logCode : logCodes){
            com.example.demo.model.entity.enums.LogCode logCode1 = com.example.demo.model.entity.enums.LogCode.getBySingleCode(logCode.getTargetCode());
            if (logCode1 == null){
                continue;
            }
            String sql = "select * from intl_log_msg_en where code = " + logCode1.getCode();
            PreparedStatement pstmt = connectionUtil.getConn().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()){
                targetList.add(logCode.getEnumCode());
            }else{
                System.out.println(logCode1.getCode() + " - - - - " + rs);
            }
        }
        for (String str : targetList){
            System.out.println(str);
        }
        connectionUtil.closeConnection();
    }

    @Test
    public void recursionSearch(){
    }

    private ConnectionUtil getConnectionUtil(){
        String driver = "";
        String url = "";
        String username = "";
        String password = "";
        ConnectionUtil connectionUtil = new ConnectionUtil(driver, url, username, password);
        return connectionUtil;
    }

    private List<LogCode> matchCodeFile(String findPath, String modelPath) {
        if (Strings.isNullOrEmpty(findPath) || Strings.isNullOrEmpty(modelPath)) {
            return new ArrayList<>();
        }
        File modelFile = new File(modelPath);
        File findFile = new File(findPath);
        if (modelFile == null || findFile == null) {
            return new ArrayList<>();
        }
        String regex = "[0-9]+(?=(, *))";
        Pattern p = Pattern.compile(regex);
        List<String> list = IoStreamUtils.getListByFileReadLine(findFile);
        List<String> modelCodes = IoStreamUtils.getListByFileReadLine(modelFile);
        List<LogCode> logCodes = new ArrayList<>();
        for (String segment : list) {
            for (String modelCode : modelCodes) {
                if (modelCode.indexOf(segment) > 0) {
                    // 匹配到了目标
                    Matcher matcher = p.matcher(modelCode);
                    if (matcher.find()) {
                        String code = matcher.group();
                        LogCode logCode = new LogCode();
                        logCode.setEnumCode(modelCode);
                        logCode.setTargetCode(Integer.parseInt(code));
                        logCodes.add(logCode);
                    }
                }
            }
        }
        return logCodes;
    }

    public class LogCode{
        String enumCode;
        Integer targetCode;

        public String getEnumCode() {
            return enumCode;
        }

        public void setEnumCode(String enumCode) {
            this.enumCode = enumCode;
        }

        public Integer getTargetCode() {
            return targetCode;
        }

        public void setTargetCode(Integer targetCode) {
            this.targetCode = targetCode;
        }
    }




}