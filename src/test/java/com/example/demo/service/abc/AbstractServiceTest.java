package com.example.demo.service.abc;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbstractServiceTest {

    Logger logger = Logger.getLogger(AbstractServiceTest.class);

    @Autowired
    private List<AbstractService> abstractService;

    @Test
    public void getString() {
        //直接使用抽象注入List，默认将会遍历注入所有对象
        for (AbstractService service : abstractService){
            System.out.println("Test List<AbstractService> foreach \n" + service.getString());
            logger.debug("test debug");
        }
    }
}