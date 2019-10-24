package com.github.losemy.rpc.register.zk;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.losemy.rpc.register.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.util.ArrayList;
import java.util.List;

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
        log.debug("connect zookeeper");
    }

    /**
     * todo 设计对应路由算法
     * @param name
     * @return
     */
    @Override
    public List<String> discover(String name) {
        // 创建 ZooKeeper 客户端
        List<String> addressList = null;
        log.debug("connect zookeeper");
        String servicePath = Constant.ZK_REGISTRY_PATH + "/" + name;
        try {
            // 获取 service 节点
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            addressList = zkClient.getChildren(servicePath);
            log.info("addressList {}", StrUtil.join(",",addressList));
            if (CollectionUtil.isEmpty(addressList)) {
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }
        } catch (Exception e){
            log.error("获取节点失败",e);
        }

        List<String> servers = new ArrayList<>();
        String data ;
        for(String address : addressList){
            data = servicePath + "/" + address;
            String server = zkClient.readData(data);
            servers.add(server);
        }
        log.info("servers {}", StrUtil.join(",",servers));

        return servers;
    }
}