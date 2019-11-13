package com.github.losemy.rpc.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.losemy.rpc.client.RpcClientFactory;
import com.github.losemy.rpc.register.zk.Constant;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lose
 * @date 2019-11-12
 **/
@Slf4j
public class ZkUtil {

    private final ZkClient zkClient;

    public ZkUtil(String zkAddress) {
        zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT ,new MyZkSerializer());
        log.info("connect zookeeper");
    }


    public boolean monitorServicePath(String serviceName){
        String servicePath = buildServicePath(serviceName);
        try {
            zkClient.subscribeChildChanges(servicePath, (parentPath, currentChilds) -> {
                //todo 缓存一份之前的会更好
                //删除删掉的节点，同时添加新增节点连接
                List<String> serverAddress = getServiceAddressByKeys(parentPath,currentChilds);
                // 不需要管理断连，由心跳机制保证？
                log.info("register changed servicePath {} address {} ",parentPath,StrUtil.join(",",serverAddress));
                serverAddress.stream().forEach( address -> {
                    String[] array = StrUtil.split(address, ":");
                    String host = array[0];
                    int port = Integer.parseInt(array[1]);
                    try {
                        //重新注册
                        log.info("auto discovery {}",serviceName);
                        RpcClientFactory.startClient(serviceName,host,port);
                    } catch (Exception e) {
                        log.error("启动服务失败",e);
                    }
                });
            });
        }catch(Exception e){
            log.error("监听失败",e);
            return false;
        }
        return true;

    }

    public List<String> getServerAddress(String serviceName) {

        String servicePath = buildServicePath(serviceName);
        // zk数据列表
        List<String> addressList = null;

        try {
            // 获取 service 节点
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            addressList = zkClient.getChildren(servicePath);
            if (CollectionUtil.isEmpty(addressList)) {
                log.info("can not find any address node on path {}",servicePath);
            }
        } catch (Exception e){
            log.error("获取节点失败",e);
        }

        return getServiceAddressByKeys(servicePath, addressList);

    }

    public void registerService(String serviceName, String serviceAddress) {
        String registryPath = Constant.ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath,true);
            log.debug("create registry node: {}", registryPath);
        }
        // 创建 service 节点（持久）
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            log.debug("create service node: {}", servicePath);
        }
        // 创建 address 节点（临时）
        String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
        log.debug("create address node: {}", addressNode);
    }


    private List<String> getServiceAddressByKeys(String servicePath, List<String> addressList) {
        List<String> serverAddress = null;
        if(addressList != null) {
            serverAddress = addressList.stream().map(address -> {
                String data = servicePath + "/" + address;
                return (String) zkClient.readData(data);
            }).collect(Collectors.toList());
        }

        log.info("servers {}", StrUtil.join(",",serverAddress));
        return serverAddress;
    }


    private String buildServicePath(String serviceName) {
        return Constant.ZK_REGISTRY_PATH + "/" + serviceName;
    }
}
