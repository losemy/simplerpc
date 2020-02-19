package com.github.losemy.rpc.spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lose
 * @date 2019-11-13
 **/
@Data
@ConfigurationProperties(prefix = "simple-rpc")
public class SimpleRpcProperties {

    private String registryAddress;
    private String serviceAddress;

}
