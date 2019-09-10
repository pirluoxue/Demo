package com.example.demo.bean;

import com.example.demo.model.entity.simple.SuperUser;
import com.example.demo.model.entity.simple.User;
import com.example.demo.model.entity.bean.TestBeanEntity;
import com.example.demo.model.entity.change.TestChangeJson;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author chen_bq
 * @description 测试一些bean功能的类
 * @create: 2019-04-01 15:16
 **/
@Component
public class TestBean {

    //测试通过@Qualifier标签注入
    //如果没有复数个同名的bean，则不需要加@Qualifier就可以注入。当使用多个返回相同的bean时，那是才需要用@Qualifier区分。详情测试参考Testbean测试类。
    @Bean
    @Qualifier("testQualifier1")
    public TestChangeJson qualifierTestChangeJson() {
        TestChangeJson testChangeJson = new TestChangeJson();
        testChangeJson.setA("this is a 1");
        testChangeJson.setB("this is b 1");
        testChangeJson.setC("this is c 1");
        return testChangeJson;
    }

    @Bean
    @Qualifier("testQualifier2")
    public TestChangeJson testQualifier2() {
        TestChangeJson testChangeJson = new TestChangeJson();
        testChangeJson.setA("this is a 2");
        testChangeJson.setB("this is b 2");
        testChangeJson.setC("this is c 2");
        return testChangeJson;
    }

    @Bean("testQualifier")
    public TestBeanEntity getTestChangeJson(@Qualifier("testQualifier1") TestChangeJson testChangeJson)
    {
        //通过@Qualifier区分该bean
        TestBeanEntity testbean = new TestBeanEntity();
        testbean.setStr(testChangeJson.getA());
        return testbean;
    }

    /*接下来写复数个且不含@Qualifier注释的bean*/

    @Bean
    public User userBean1(){
        User user = new User();
        user.setStr("userBean1");
        return user;
    }

    @Bean
    public User userBean2(){
        User user = new User();
        user.setStr("userBean2");
        return user;
    }

    //此时若不使用@Qualifier，则报错。
    //另外也可以直接使用方法名代替上级的@Qualifier。但是@Qualifier的优先级高于方法名，如果使用了@Qualifier，那么方法名将不再能被注入
    @Bean
    public User userBean(@Qualifier("userBean1") User user){
        return user;
    }

    /*@primary的使用测试*/
    /*@Primary 作用就是设置默认的bean，即存在多个可注入的bean时，又没有使用@Qualifier指定的时候，默认注入该bean*/
    @Bean
    public SuperUser superUser1(){
        SuperUser superUser = new SuperUser();
        superUser.setStr("this is superUser 1");
        return superUser;
    }

    @Bean
    @Primary
    public SuperUser superUser2(){
        SuperUser superUser = new SuperUser();
        superUser.setStr("this is superUser 2");
        return superUser;
    }

    @Bean
    public SuperUser getSuperUser(SuperUser superUser){
        superUser.setStr("change the superUser");
        return superUser;
    }


}
