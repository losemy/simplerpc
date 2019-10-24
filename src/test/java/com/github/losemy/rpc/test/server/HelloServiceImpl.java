package com.github.losemy.rpc.test.server;


import com.github.losemy.rpc.server.RpcService;
import com.github.losemy.rpc.test.api.HelloService;
import com.github.losemy.rpc.test.api.Person;
import lombok.extern.slf4j.Slf4j;

@RpcService(value= HelloService.class)
@Slf4j
public class HelloServiceImpl implements HelloService {

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
