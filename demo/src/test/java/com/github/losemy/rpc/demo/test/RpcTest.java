package com.github.losemy.rpc.demo.test;

import com.github.losemy.rpc.demo.client.TestBean;
import com.github.losemy.rpc.demo.client.TestBean1;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lose
 * @date 2019-11-13
 **/
@Slf4j
public class RpcTest extends AbstractSpringBootTest {

    @Autowired
    private TestBean testBean;

    @Autowired
    private TestBean1 testBean1;

    @Test
    public void testHello(){
        String name = "test";

        Assert.assertEquals("Hello! " + name, testBean.hello(name));
        Assert.assertEquals("Hello! " + name, testBean1.hello(name));

    }

}
