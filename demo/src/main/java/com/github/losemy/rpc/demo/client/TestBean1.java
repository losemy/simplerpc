package com.github.losemy.rpc.demo.client;

import com.github.losemy.rpc.client.RpcReference;
import com.github.losemy.rpc.demo.api.HelloService;
import com.github.losemy.rpc.demo.api.WorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lose
 * @date 2019-10-24
 **/
@Slf4j
@Component
public class TestBean1 {

    @RpcReference
    private HelloService helloService;

    @RpcReference
    private WorldService worldService;

    public String hello(String name){
        log.info(name);
        worldService.hello(name);
        return helloService.hello(name);
    }
}
