package com.github.losemy.rpc.demo.api;

public interface HelloService {

    String hello(String name);

    String hello(Person person);
}
