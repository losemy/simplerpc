package com.github.losemy.rpc.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.losemy.rpc.client.RpcClientFactory;
import com.github.losemy.rpc.register.zk.Constant;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
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
        zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        log.info("connect zookeeper");
    }


    public boolean monitorServicePath(String serviceName){
        String servicePath = buildServicePath(serviceName);
        try {
            zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                @Override
                public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                    List<String> serverAddress = getServiceAddressByKeys(parentPath,currentChilds);
                    // 不需要管理断连，由心跳机制保证？
                    log.info("register changed servicePath {} address {} ",parentPath,StrUtil.join(",",serverAddress));
                    serverAddress.stream().forEach( address ->{
                        String[] array = StrUtil.split(address, ":");
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);
                        try {
                            //重新注册，不存在的会被发现

                            RpcClientFactory.startClient(serviceName,host,port);
                        } catch (Exception e) {
                            log.error("启动服务失败",e);
                        }
                    });
                }
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
            log.info("addressList {}", StrUtil.join(",",addressList));
            if (CollectionUtil.isEmpty(addressList)) {
                log.info("can not find any address node on path {}",servicePath);
            }
        } catch (Exception e){
            log.error("获取节点失败",e);
        }

        return getServiceAddressByKeys(servicePath, addressList);

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
