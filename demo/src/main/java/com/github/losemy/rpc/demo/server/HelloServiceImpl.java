package com.github.losemy.rpc.demo.server;


import com.github.losemy.rpc.demo.api.HelloService;
import com.github.losemy.rpc.demo.api.Person;
import com.github.losemy.rpc.server.RpcService;
import lombok.extern.slf4j.Slf4j;

@RpcService(value= HelloService.class)
@Slf4j
public class HelloServiceImpl implements HelloService {

    /**
     * 注释3
     * @param name
     * @return
     */
    @Override
    public String hello(String name) {
        log.info(name);
        return "Hello! " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }
}
