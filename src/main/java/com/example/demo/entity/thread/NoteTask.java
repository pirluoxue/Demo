package com.example.demo.entity.thread;

import com.example.demo.util.IntelligentUtil;
import com.example.demo.util.SpringContextUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author chen_bq
 * @description 多线程实现实体
 * @create: 2019-03-22 15:46
 **/
@Data
public class NoteTask implements Callable<Boolean> {

    private RestTemplate restTemplate;
    private List<String> noteUrlList;
    private int threadNumber;
    private String fileName;
    private String path;

//    public static void main(String[] args) {
//        NoteTask noteTask = new NoteTask();
//        noteTask.setFileName("test.txt");
//        noteTask.setPath("e:\\NotePack\\");
//        noteTask.writeText("asd\ntest\nwow", 1);
//    }

    private void writeText(String text, int childNumber){
        String fullFileName = this.fileName.replace(".", childNumber + ".");
        File outFile = new File(this.path + fullFileName);
        //不存在则创建文件夹目录
        if(!outFile.getParentFile().exists()){
            if (!outFile.getParentFile().mkdirs()) {
                System.out.println(
                        "- ERROR creating output direcrory '" + outFile.getParentFile().getAbsolutePath() + "'");
            }
        }
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
            out.append(text);
            //关闭流
            out.flush();
            out.close();
            System.out.println("fullFileName 完成" + Thread.currentThread().getName());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public NoteTask(){
        this.fileName = "test.txt";
        this.path = "d:\\notePack\\";
    }

    public NoteTask(List<String> noteUrlList){
        this.noteUrlList = noteUrlList;
        this.fileName = "test.txt";
        this.path = "d:\\notePack\\";
    }

    public NoteTask(List<String> noteUrlList, int threadNumber){
        this.noteUrlList = noteUrlList;
        this.threadNumber = threadNumber;
        this.fileName = "test.txt";
        this.path = "d:\\notePack\\";
    }

    public  NoteTask(List<String> noteUrlList, int threadNumber, String fileName, String path){
        this.noteUrlList = noteUrlList;
        this.threadNumber = threadNumber;
        this.fileName = fileName;
        this.path = path;
    }

    @Override
    public Boolean call() throws Exception {
        restTemplate = SpringContextUtils.getBean(RestTemplate.class);
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0 ;i < noteUrlList.size() ; i++){
            System.out.println("请求地址： " + noteUrlList.get(i));
            ResponseEntity<String> response = restTemplate.getForEntity(noteUrlList.get(i), String.class);
            if(response.getStatusCode() == HttpStatus.OK){
                String note = IntelligentUtil.getMatchByRegexToString(response.getBody(), IntelligentUtil.NOTE_BR_REGEX);
                stringBuffer.append(note);
            }else{
                throw new RuntimeException("拉取网址路径异常");
            }
            System.out.println("拉取： " + noteUrlList.get(i) + " 完成");
        }
        writeText(stringBuffer.toString(),threadNumber);
        return true;
    }
}
