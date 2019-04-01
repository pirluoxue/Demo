package com.example.demo.entity;

import com.example.demo.entity.clone.ComplexCloneEntity;
import com.example.demo.entity.clone.SimpleCloneEntity;
import com.example.demo.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SimpleCloneEntity.class)
public class CloneTestEntityTest {

    @Test
    public void testSimpleCloneProductivity(){
        LocalDateTime localDateTime = LocalDateTime.now();
        for(int i = 0 ; i < 10000000; i++){
            SimpleCloneEntity simpleTestEntity = new SimpleCloneEntity();
            simpleTestEntity.setStr("new");
        }
        LocalDateTime newTime = localDateTime.now();
        Duration duration = Duration.between(localDateTime,newTime);
        System.out.println("new用时：" + duration);
        System.out.println(SimpleCloneEntity.getSimpleCloneEntity());
        for(int i = 0 ; i < 10000000; i++) {
            SimpleCloneEntity simpleCloneEntity = SimpleCloneEntity.getSimpleCloneEntity();
            simpleCloneEntity.setStr("clone");
        }
        LocalDateTime cloneTime = localDateTime.now();
        duration = Duration.between(newTime,cloneTime);
        System.out.println("Clone用时：" + duration);
        System.out.println(SimpleCloneEntity.getSimpleCloneEntity());
        for(int i = 0 ; i < 10000000; i++){
            SimpleCloneEntity simpleCloneEntity = SimpleCloneEntity.getInstance();
            simpleCloneEntity.setStr("instance");
        }
        LocalDateTime instanceTime = localDateTime.now();
        duration = Duration.between(cloneTime,instanceTime);
        System.out.println("instance用时：" + duration);
        System.out.println(SimpleCloneEntity.getInstance());
    }

    @Test
    public void testComplexCloneProductivity(){
        LocalDateTime localDateTime = LocalDateTime.now();
        for(int i = 0 ; i < 100 * 100; i++){
            ComplexCloneEntity complexCloneEntity = new ComplexCloneEntity();
            complexCloneEntity.setStr("new");
        }
        LocalDateTime newTime = localDateTime.now();
        Duration duration = Duration.between(localDateTime,newTime);
        System.out.println("new用时：" + duration);
        System.out.println(new ComplexCloneEntity());
        for(int i = 0 ; i < 100 * 100; i++) {
            ComplexCloneEntity simpleCloneEntity = ComplexCloneEntity.getComplexCloneEntity();
            simpleCloneEntity.setStr("clone");
        }
        LocalDateTime cloneTime = localDateTime.now();
        duration = Duration.between(newTime,cloneTime);
        System.out.println("Clone用时：" + duration);
        System.out.println(ComplexCloneEntity.getComplexCloneEntity());
        for(int i = 0 ; i < 100 * 100; i++){
            ComplexCloneEntity simpleCloneEntity = ComplexCloneEntity.getInstance();
            simpleCloneEntity.setStr("instance");
        }
        LocalDateTime instanceTime = localDateTime.now();
        duration = Duration.between(cloneTime,instanceTime);
        System.out.println("instance用时：" + duration);
        System.out.println(ComplexCloneEntity.getInstance());

        System.out.println("再次ComplexCloneEntity: " + ComplexCloneEntity.getComplexCloneEntity());
    }


}