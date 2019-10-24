package com.github.losemy.rpc.client;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理channel
 * @author lose
 * @date 2019-10-23
 **/
@Slf4j
public class ConnectManager {

    private static volatile ConnectManager connectManager;

    /**
     * key 为 interfaceName + version
     * value 为对应服务的channel
     */
    private Map<String, List<RpcClientHandler>> rpcClientHandlerMap = new ConcurrentHashMap<>();

    /**
     * key 为 interfaceName
     * value 为对应服务的list
     */
    private Map<String,List<String>> rpcServerMap = new ConcurrentHashMap<>();

    public static ConnectManager getInstance() {
        if (connectManager == null) {
            synchronized (ConnectManager.class) {
                if (connectManager == null) {
                    connectManager = new ConnectManager();
                }
            }
        }
        return connectManager;
    }

    public void addServer(String name,String server){
        List<String> servers = rpcServerMap.get(name);
        if(servers == null){
            servers = new ArrayList<>();
        }
        servers.add(server);
        log.info("servers {} , size {}", name ,servers.size());
        rpcServerMap.put(name,servers);
    }

    public List<String> getServers(String name){
        return rpcServerMap.get(name);
    }


    public void addRpcClientHandler(RpcClientHandler rpcClientHandler,String name){
        List<RpcClientHandler> handlers = rpcClientHandlerMap.get(name);
        if(handlers == null){
            handlers = new ArrayList<>();
        }
        handlers.add(rpcClientHandler);
        log.info("handlers {} , size {}", name ,handlers.size());
        rpcClientHandlerMap.put(name,handlers);
    }

    public RpcClientHandler getRpcClientHandler(String name){
        log.info("getRpcClientHandler name {}" , name);
        RpcClientHandler handler = null;
        List<RpcClientHandler> handlers = rpcClientHandlerMap.get(name);
        if(handlers.size() > 0 ){
            handler = handlers.get(RandomUtil.randomInt(handlers.size()));
        }
        log.info("handler== {}", handler);
        return handler;
    }




}
