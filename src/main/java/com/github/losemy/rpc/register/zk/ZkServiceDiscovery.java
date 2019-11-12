package com.github.losemy.rpc.register.zk;


import com.github.losemy.rpc.loadbalance.LoadBalanceFactory;
import com.github.losemy.rpc.register.ServiceDiscovery;
import com.github.losemy.rpc.util.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 基于 ZooKeeper 的服务发现接口实现
 *
 * @author lose
 * @date 2019-10-23
 */
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {

    @Autowired
    private ZkUtil zkUtil;
    /**
     * @param serviceName
     * @return
     */
    @Override
    public String discover(String serviceName) {
        // 创建 ZooKeeper 客户端
        return loadBalance(zkUtil.getServerAddress(serviceName));
    }

    @Override
    public List<String> findAllServer(String serviceName) {
        return zkUtil.getServerAddress(serviceName);
    }

    public String loadBalance(List<String> serverAddress) {
        // 根据数据记录 目前 就是简单一刀切 设计策略算法 根据不同类型发挥不同
        return LoadBalanceFactory.getLoadBalanceByName("random").loadBalance(serverAddress);
    }


}