package com.example.demo.util;

import com.example.demo.entity.BigDecimalEntity;
import com.example.demo.entity.thread.SimpleTask;
import com.mchange.v2.holders.ThreadSafeShortHolder;
import netscape.security.UserTarget;

import javax.swing.plaf.metal.MetalCheckBoxIcon;
import javax.swing.text.html.HTMLDocument;
import java.net.Authenticator;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chen_bq
 * @description 智能工具类
 * @create: 2019-03-23 10:00
 **/
public class IntelligentUtil{

    //作为频繁程度的参考值
    public static double REFERENCE_STANDARD = 0.5;
    public static final String URL_LIST_REGEX = "(href=\")+\\S*?\"";
    public static final String NOTE_BR_REGEX = "(<br)+.*(<br)?";

    /**
     * 解析并获得有效url
     * @param totalUrl
     * @return
     */
    public static String analysisEffectPreUrl(List<String> totalUrl){
        Map<String, Integer> map = new HashMap();
        for(int i = 0 ; i < totalUrl.size() ; i++){
            String url = removePreAndSuf(totalUrl.get(i));
            if(StringUtil.isNullOrEmpty(url)){
                continue;
            }
            String key = getKey(url);
            if(StringUtil.isNullOrEmpty(key)){
                continue;
            }
            Integer num = map.get(key);
            if(num == null){
                map.put(key, 1);
            }else {
                map.put(key, num + 1);
            }
        }
        String effectPreUrl = "";
        Integer frequentest = 1;
        for (String key:map.keySet()){
            if(map.get(key) > frequentest){
                frequentest = map.get(key);
                effectPreUrl = key;
            }
        }
        return effectPreUrl;
    }

    /**
     * 获得所有有效的url
     * @param totalUrl
     * @return
     */
    public static List<String> getTotalEffectUrl(List<String> totalUrl, String indexUrl){
        List<String> effectUrl = new ArrayList<>();
        String effectPreUrl = analysisEffectPreUrl(totalUrl);
        for(int i = 0 ; i < totalUrl.size() ;i++){
            String url = removePreAndSuf(totalUrl.get(i));
            //是否包含有效前缀
            if(url.contains(effectPreUrl)){
                url = buildUrl(indexUrl, url);
                effectUrl.add(url);
            }
        }
        return effectUrl;
    }

    public static List<String> getTotalEffectUrl(List<String> totalUrl){
        return getTotalEffectUrl(totalUrl, null);
    }

    private static String buildUrl(String indexUrl, String suffixUrl){
        if(indexUrl == null){
            return suffixUrl;
        }
        //是否为完整地址
        if(suffixUrl.startsWith("//")){
            suffixUrl = indexUrl.substring(0, indexUrl.indexOf("//") + "//".length());
        }else if(!suffixUrl.startsWith("http")){
            //拼接url
            String[] suffix = suffixUrl.split("/");
            StringBuffer noRepeatUrl = new StringBuffer(indexUrl);
            for(int i = 0 ; i < suffix.length ; i++){
                //不重复的部分为正确内容
                if(!StringUtil.isNullOrEmpty(suffix[i]) && !indexUrl.contains(suffix[i])){
                    noRepeatUrl.append(suffix[i]);
                }
            }
            return noRepeatUrl.toString();
        }
        return suffixUrl;
    }

    /**
     * 获得通用key，默认使用最后一个”/"前的数据作为key
     * @param url
     * @return
     */
    private static String getKey(String url){
        int index = url.lastIndexOf("/");
        if(index < 0){
            return null;
        }
        return url.substring(0, index);
    }

    /**
     * 清除href标签的Url的前后缀
     * @param hrefUrl
     * @return
     */
    private static String removePreAndSuf(String hrefUrl){
        //清空前缀
        hrefUrl = hrefUrl.replace("href=\"","");
        //清空后缀
        hrefUrl = hrefUrl.replace("\"","");
        return hrefUrl;
    }

    public static String getMatchByRegexToString(String html, String regex){
        StringBuffer stringBuffer = new StringBuffer();
        //装载解释器
        Pattern pattern = Pattern.compile(NOTE_BR_REGEX);
        //匹配
        Matcher matcher = pattern.matcher(html);
        //尝试找到匹配模式的输入序列的下一个子序列。
        while (matcher.find()){
            //返回与上一个匹配匹配的输入子序列。
            stringBuffer.append(matcher.group());
        }
        return stringBuffer.toString();
    }

    public static List<String> getMatchByRegexToList(String html, String regex){
        List<String> list = new ArrayList<>();
        //装载解释器
        Pattern pattern = Pattern.compile(regex);
        //匹配
        Matcher matcher = pattern.matcher(html);
        //类似iterator迭代器
        //尝试找到匹配模式的输入序列的下一个子序列。
        while (matcher.find()){
            //返回与上一个匹配匹配的输入子序列。
            list.add(matcher.group());
        }
        return list;
    }

    public static void main(String[] args) {
        List<FutureTask> taskList = new ArrayList<>();
        for(int i = 0 ; i < 20 ; i++){
            SimpleTask simpleTask = new SimpleTask();
            FutureTask futureTask = new FutureTask(simpleTask);
            Thread thread = new Thread(futureTask);
            taskList.add(futureTask);
            thread.start();
        }
        while(true){
            int i = 0;
            for (i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).isDone()) {
                    continue;
                }
                break;
            }
            //均完成加载
            if(i == taskList.size()){
                break;
            }
        }
        System.out.println("全都完成了");
    }

}
