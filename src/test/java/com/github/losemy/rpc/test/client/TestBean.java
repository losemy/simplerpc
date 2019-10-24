package com.github.losemy.rpc.test.client;


import com.github.losemy.rpc.client.RpcReference;
import com.github.losemy.rpc.test.api.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lose
 * @date 2019-10-23
 **/
@Slf4j
public class TestBean {

    @RpcReference
    private HelloService helloService;

    public String hello(String name){
        log.info(name);
        return helloService.hello(name);
    }
}
