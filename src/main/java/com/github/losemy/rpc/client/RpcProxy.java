package com.github.losemy.rpc.client;

import com.github.losemy.rpc.common.bean.RpcRequest;
import com.github.losemy.rpc.common.bean.RpcResponse;
import com.github.losemy.rpc.register.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author lose
 * @date 2019-10-23
 **/
@Slf4j
public class RpcProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);

    private String serviceAddress;

    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass) {
        return create(interfaceClass, "");
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass, final String serviceVersion) {
        // 创建动态代理对象
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 创建 RPC 请求对象并设置请求属性
                        RpcRequest request = new RpcRequest();
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setInterfaceName(method.getDeclaringClass().getName());
                        request.setServiceVersion(serviceVersion);
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);
                        // 创建 RPC 客户端对象并发送 RPC 请求
                        String name = ServiceUtil.buildServiceName(request.getInterfaceName(),request.getServiceVersion());

                        log.info("invoke {}" , name);
                        RpcClientHandler handler = ConnectManager.getInstance().getRpcClientHandler(name);

                        log.info("handler {}" , handler != null );
                        CompletableFuture<RpcResponse> rpcResponse = handler.sendRequest(request);

                        RpcResponse response = rpcResponse.get();
                        if(response.getException() == null) {
                            return response.getResult();
                        }else{
                            throw response.getException();
                        }
                    }

                }
        );
    }
}
