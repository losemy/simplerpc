package com.github.losemy.rpc.test.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lose
 * @date 2019-10-23
 **/
public class Test {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-service.xml");
    }
}
