package com.github.losemy.rpc.demo.test;

import com.github.losemy.rpc.demo.client.TestBean;
import com.github.losemy.rpc.demo.client.TestBean1;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lose
 * @date 2019-11-13
 **/
public class RpcTest extends AbstractSpringBootTest {

    @Autowired
    private TestBean testBean;

    @Autowired
    private TestBean1 testBean1;

    @Test
    public void testHello(){
        Assert.assertEquals("Hello! 123",testBean.hello("123"));
        Assert.assertEquals("Hello! 123",testBean1.hello("123"));
    }
}
