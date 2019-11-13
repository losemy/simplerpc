package com.github.losemy.rpc.spring.autoconfigure;

import com.github.losemy.rpc.client.ClientManager;
import com.github.losemy.rpc.client.RpcClient;
import com.github.losemy.rpc.client.RpcProxy;
import com.github.losemy.rpc.register.ServiceAutoDiscovery;
import com.github.losemy.rpc.register.ServiceDiscovery;
import com.github.losemy.rpc.register.ServiceRegistry;
import com.github.losemy.rpc.register.zk.ZkServiceAutoDiscovery;
import com.github.losemy.rpc.register.zk.ZkServiceDiscovery;
import com.github.losemy.rpc.register.zk.ZkServiceRegistry;
import com.github.losemy.rpc.server.RpcServer;
import com.github.losemy.rpc.spring.config.SimpleRpcProperties;
import com.github.losemy.rpc.util.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lose
 * @date 2019-11-13
 * 配置zk方式的自动装配
 **/
@Configuration
@EnableConfigurationProperties(SimpleRpcProperties.class)
@ConditionalOnClass({ RpcClient.class, RpcServer.class })
@ConditionalOnProperty(prefix = "simple-rpc", value = "registry-address")
@Slf4j
public class SimpleRpcAutoConfiguration {

    @Autowired
    private SimpleRpcProperties simpleRpcProperties;

    @Bean
    @ConditionalOnMissingBean
    public ZkUtil zkUtil(){
        log.info("simpleRpcProperties {}",simpleRpcProperties);
        return new ZkUtil(simpleRpcProperties.getRegistryAddress());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "simple-rpc", value = "serviceAddress")
    public ServiceRegistry serviceRegistry(){
        log.info("service-address serviceRegistry");
        return new ZkServiceRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "simple-rpc", value = "serviceAddress")
    public RpcServer rpcServer(){
        log.info("service-address rpcServer");
        return new RpcServer(simpleRpcProperties.getServiceAddress(),serviceRegistry());
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceDiscovery serviceDiscovery(){
        return new ZkServiceDiscovery();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceAutoDiscovery serviceAutoDiscovery(){
        return new ZkServiceAutoDiscovery();
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcProxy rpcProxy(){
        return new RpcProxy(serviceDiscovery());
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientManager clientManager(){
        return new ClientManager();
    }



}
