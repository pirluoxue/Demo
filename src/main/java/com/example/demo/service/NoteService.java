package com.example.demo.service;

import org.springframework.scheduling.annotation.Async;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-22 15:34
 **/
public interface NoteService {


    public boolean collectionNoteByIndexUrl(String indexUrl, String path, String fileName);



}
