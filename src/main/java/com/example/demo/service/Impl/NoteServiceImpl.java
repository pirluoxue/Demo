package com.example.demo.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.thread.NoteThread;
import com.example.demo.service.NoteService;
import com.example.demo.util.SpringContextUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-22 15:35
 **/
@Service
public class NoteServiceImpl implements NoteService {

    @Override
    @Async
    public boolean collectionNoteByIndexUrl(String indexUrl) {
        List<String> indexOfNoteUrl = getIndexOfNoteUrl(indexUrl);
        for(int i = 0 ;i < 10 ; i++){
            NoteThread noteThread = new NoteThread(indexUrl);
            Thread thread = new Thread(noteThread);
            thread.start();
        }
        while(true){

        }
//        return true;
    }

    private List<String> getIndexOfNoteUrl(String indexUrl){
        RestTemplate restTemplate = SpringContextUtils.getBean(RestTemplate.class);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(indexUrl, String.class);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException("目录网址请求异常！！！");
        }
        String html = responseEntity.getBody();

        return null;
    }

    private List<String> findNoteUrlList(String html){
        //正则表达式
        String regex = "(href=\")+.*(\">)";
        //装载解释器
        Pattern p = Pattern.compile(regex);
        //匹配
        Matcher m = p.matcher(html);
        List<String> urlList = new ArrayList<>();
        //类似iterator迭代器
        //尝试找到匹配模式的输入序列的下一个子序列。
        while (m.find()){
            //返回与上一个匹配匹配的输入子序列。
            urlList.add(m.group());
        }
        urlList = deleteFeatures(urlList);
        return urlList;
    }

    private List<String> deleteFeatures(List<String> orginList){
        List<String> resultList = new ArrayList<>();
        int prefixNum = "href=\"".length();
        int suffixNum = "\">".length();
        for(String url:orginList){
            url = url.substring(prefixNum, url.length() - suffixNum);
            resultList.add(url);
        }
        return resultList;
    }

    private List<String> removeInterfere(List<String> orginList){

    }
}
