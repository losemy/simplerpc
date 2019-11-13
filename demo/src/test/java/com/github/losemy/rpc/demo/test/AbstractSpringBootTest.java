package com.github.losemy.rpc.demo.test;

import com.github.losemy.rpc.demo.DemoApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lose
 * @date 2019-10-30
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class AbstractSpringBootTest {


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }



}
