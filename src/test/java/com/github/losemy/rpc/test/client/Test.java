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

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
        TestBean testBean = context.getBean(TestBean.class);
        TestBean1 testBean1 = context.getBean(TestBean1.class);

        int i =0;
        while(true) {
            if(i++ > 100){
                break;
            }
            try {
                String result = testBean.hello("World");
                log.info(result);

                String result1 = testBean1.hello("World");
                log.info(result1);
                Thread.currentThread().sleep(10000L);
                log.info("======================================");
            }catch(Exception e){
                log.error("error============",e);
                Thread.currentThread().sleep(10000L);
            }
        }
    }
}
