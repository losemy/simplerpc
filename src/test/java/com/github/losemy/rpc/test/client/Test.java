package com.github.losemy.rpc.test.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lose
 * @date 2019-10-23
 **/
@Slf4j
public class Test {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
        TestBean testBean = context.getBean(TestBean.class);
        String result = testBean.hello("World");
        log.info(result);
        TestBean1 testBean1 = context.getBean(TestBean1.class);
        String result1 = testBean1.hello("World");
        log.info(result1);
    }
}
