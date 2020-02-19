package com.github.losemy.rpc.demo.server;

import com.github.losemy.rpc.demo.api.WorldService;
import com.github.losemy.rpc.server.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lose
 * @date 2019-11-11
 **/
@RpcService(value= WorldService.class)
@Slf4j
public class WorldServiceImpl implements WorldService {
    @Override
    public String hello(String name) {
        log.info("name {}",name);
        return name + " good";
    }
}
