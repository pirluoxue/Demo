package com.example.demo.service.Impl;

import com.example.demo.model.thread.NoteTask;
import com.example.demo.service.NoteService;
import com.example.demo.util.IntelligentUtil;
import com.example.demo.util.SpringContextUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-22 15:35
 **/
@Service
public class NoteServiceImpl implements NoteService {

    @Override
    @Async
    public boolean collectionNoteByIndexUrl(String indexUrl, String path, String fileName) {
        List<String> indexOfNoteUrl = getIndexOfNoteUrl(indexUrl);
        int everyThreadRunningNumber = indexOfNoteUrl.size() / 9;
        List<FutureTask> taskList = new ArrayList<>();
        for(int i = 0 ;i < 10 ; i++){
            NoteTask noteTask = new NoteTask();
            if(i == 9){
                noteTask = new NoteTask(indexOfNoteUrl.subList(i*everyThreadRunningNumber,indexOfNoteUrl.size()), i, path, fileName);
            }else{
                noteTask = new NoteTask(indexOfNoteUrl.subList(i*everyThreadRunningNumber,(i+1)*everyThreadRunningNumber), i,path, fileName);
            }
            FutureTask task = new FutureTask(noteTask);
            taskList.add(task);
            Thread thread = new Thread(task);
            thread.start();
        }
        while (true) {
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
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("下载完成");
        return true;
    }

    private List<String> getIndexOfNoteUrl(String indexUrl){
        RestTemplate restTemplate = SpringContextUtils.getBean(RestTemplate.class);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(indexUrl, String.class);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException("目录网址请求异常！！！");
        }
        String html = responseEntity.getBody();
        List<String> urlList = findNoteUrlList(html, indexUrl);
        return findNoteUrlList(html, indexUrl);
    }

    private List<String> findNoteUrlList(String html, String indexUrl){
        List<String> urlList = IntelligentUtil.getMatchByRegexToList(html, IntelligentUtil.URL_LIST_REGEX);
        urlList = deleteFeatures(urlList, indexUrl);
        return urlList;
    }

    private List<String> deleteFeatures(List<String> orginList, String indexUrl){
        List<String> resultList = new ArrayList<>();
        //"href=\"".length();
        int prefixNum = 6;
        //"\">".length();
        int suffixNum = 1;
        for(String url:orginList){
            url = url.substring(prefixNum, url.length() - suffixNum);
            resultList.add(url);
        }
        return removeInterfere(resultList, indexUrl);
    }

    private List<String> removeInterfere(List<String> originList){
        return IntelligentUtil.getTotalEffectUrl(originList);
    }

    private List<String> removeInterfere(List<String> originList, String indexUrl){
        return IntelligentUtil.getTotalEffectUrl(originList, indexUrl);
    }
}
