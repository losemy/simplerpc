package com.github.losemy.rpc.register.zk;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.losemy.rpc.register.ServiceDiscovery;
import com.github.losemy.rpc.register.zk.strategy.LoadBalanceFactory;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于 ZooKeeper 的服务发现接口实现
 *
 * @author lose
 * @date 2019-10-23
 */
@Slf4j
public class ZooKeeperServiceDiscovery implements ServiceDiscovery {

    private final ZkClient zkClient;

    public ZooKeeperServiceDiscovery(String zkAddress) {
        zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        log.info("connect zookeeper");
    }

    /**
     * @param serviceName
     * @return
     */
    @Override
    public String discover(String serviceName) {
        // 创建 ZooKeeper 客户端
        return loadBalance(getServerAddress(serviceName));
    }

    @Override
    public List<String> findAllServer(String serviceName) {
        return getServerAddress(serviceName);
    }

    public String loadBalance(List<String> serverAddress) {
        // 根据数据记录 目前 就是简单一刀切 设计策略算法 根据不同类型发挥不同
        return LoadBalanceFactory.getLoadBalanceByName("random").loadBalance(serverAddress);
    }

    /**
     * 获取服务列表
     * @param serviceName
     * @return
     */
    private List<String> getServerAddress(String serviceName) {

        String servicePath = Constant.ZK_REGISTRY_PATH + "/" + serviceName;
        // zk数据列表
        List<String> addressList = null;
        log.debug("connect zookeeper");

        try {
            // 获取 service 节点
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            addressList = zkClient.getChildren(servicePath);
            log.info("addressList {}", StrUtil.join(",",addressList));
            if (CollectionUtil.isEmpty(addressList)) {
                log.info("can not find any address node on path {}",servicePath);
            }
        } catch (Exception e){
            log.error("获取节点失败",e);
        }

        // zk数据

        // 修改为1.8语法 更加清晰
        List<String> serverAddress = addressList.stream().map( address -> {
            String data = servicePath + "/" + address;
            return (String) zkClient.readData(data);
        }).collect(Collectors.toList());

        log.info("servers {}", StrUtil.join(",",serverAddress));
        return serverAddress;

    }

}