package com.example.demo.entity.thread;

import com.example.demo.util.SpringContextUtils;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

/**
 * @author chen_bq
 * @description 多线程实现实体
 * @create: 2019-03-22 15:46
 **/
@Data
public class NoteThread implements Runnable {

    private RestTemplate restTemplate;
    private String indexUrl;


    @Override
    public void run() {
        restTemplate = SpringContextUtils.getBean(RestTemplate.class);



    }

    public NoteThread(String indexUrl){
        this.indexUrl = indexUrl;
    }
}
