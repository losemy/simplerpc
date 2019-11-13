package com.github.losemy.rpc.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lose
 * @date 2019-11-11
 **/
@Slf4j
public class RpcClientFactory {

    private static Map<String,RpcClientHandler> addressHandler = new ConcurrentHashMap<>();

    public static RpcClientHandler startClient(String name, String host, int port) throws Exception {
        RpcClientHandler handler = null;
        // address 格式
        String address = buildAddress(host, port);
        // 需要统计的是所有的是否都在
        log.info("contains {}",CollUtil.contains(addressHandler.keySet(),address));
        if(!CollUtil.contains(addressHandler.keySet(),address)){
            RpcClient client = new RpcClient(host,port,name);
            client.start();
            handler = client.getHandler();
        }else{
            log.info("address already started name ==> {}:{}",name,host,port);
            handler = addressHandler.get(address);
        }
        return handler;

    }

    private static String buildAddress(String host, int port) {
        return host + ":" +  port;
    }

    public static RpcClientHandler getHandlerByAddress(String name,String host, int port) throws Exception {
        log.info("name {} {}:{}",name,host,port);
        String address = buildAddress(host, port);
        RpcClientHandler rpcClientHandler = addressHandler.get(address);
        if(rpcClientHandler == null){
            log.info("对应服务{} 对应服务地址{} 未开启 尝试重连 ",name,address);
            //有调用且选中才会开启，正常数据可能被本地cache
            rpcClientHandler = startClient(name,host,port);
        }
        return rpcClientHandler;

    }

    public static void addRpcClientHandler(RpcClientHandler handler,String host, int port){
        String address = buildAddress(host, port);
        addressHandler.put(address,handler);
    }


    public static void removeRpcClientHandler(RpcClientHandler handler){
        for(Map.Entry<String,RpcClientHandler> rpcClientHandlerEntry : addressHandler.entrySet()){
            if(ObjectUtil.equal(handler,rpcClientHandlerEntry.getValue())){
                addressHandler.remove(rpcClientHandlerEntry.getKey());
            }
        }
    }


}
