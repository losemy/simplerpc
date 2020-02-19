package com.github.losemy.rpc.register.zk;


import com.github.losemy.rpc.register.ServiceRegistry;
import com.github.losemy.rpc.util.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基于 ZooKeeper 的服务注册接口实现
 *
 * @author lose
 * @date 2019-10-23
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {


    @Autowired
    private ZkUtil zkUtil;

    @Override
    public void register(String serviceName, String serviceAddress) {
        // 创建 registry 节点（持久）
        zkUtil.registerService(serviceName, serviceAddress);
    }

}