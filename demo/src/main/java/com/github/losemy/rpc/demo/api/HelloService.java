package com.github.losemy.rpc.demo.api;

public interface HelloService {

    /**
     * hello
     * @param name
     * @return
     */
    String hello(String name);

    String hello(Person person);

}
