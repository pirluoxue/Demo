package com.example.demo.util.toy;

import com.example.demo.model.entity.enums.LogCode;
import com.example.demo.model.entity.simple.MatchInfo;
import com.example.demo.util.io.IoStreamUtils;
import com.google.common.base.Strings;

import javax.jnlp.IntegrationService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author chen_bq
 * @description 递归工具类
 * @create: 2019/12/13 18:46
 **/
public class RecursionUtils {

    public List listMatchEnunsByFile(String fileName){
        if (Strings.isNullOrEmpty(fileName)){
            return new ArrayList();
        }
        File file = new File(fileName);
        if (file == null){
            return new ArrayList();
        }
        List<String> list = IoStreamUtils.getListByFileReadLine(file);
        List<MatchInfo> logCodes = new ArrayList<>();
        for (String segment : list) {
            String queryParam = foreachSearch(segment);
            MatchInfo info = new MatchInfo();
            info.setMatchResource(segment);
            try {
                Integer code = Integer.parseInt(queryParam);
                info.setMatchCode(code);
            }catch (Exception e){

            }
            logCodes.add(info);
        }
        return logCodes;
    }

    private static String foreachSearch(String segment){
        Integer code = findLogCode(segment);
        if (code != null){
            return String.valueOf(code);
        }
        return null;
    }

    private static Integer findLogCode(String segment){
        LogCode logCode = LogCode.valueOf(segment);
        return logCode.getCode();
    }

}
