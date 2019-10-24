package com.github.losemy.rpc.client;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.losemy.rpc.register.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author lose
 * @date 2019-10-23
 **/
@Slf4j
public class ClientFactory extends InstantiationAwareBeanPostProcessorAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {

        log.info("test inject");
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (field.isAnnotationPresent(RpcReference.class)) {
                    // valid
                    Class interfaceName = field.getType();
                    if (!interfaceName.isInterface()) {
                        log.error("声明类不是接口异常");
                    }

                    RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                    String version = rpcReference.version();
                    String inName = interfaceName.getName();
                    String name = ServiceUtil.buildServiceName(inName,version);
                    //代理

                    log.info("interfaceName {}" , inName);

                    //获取数据
                    RpcProxy proxy = applicationContext.getBean(RpcProxy.class);
                    Object serviceProxy = proxy.create(interfaceName,version);

                    ServiceDiscovery serviceDiscovery = applicationContext.getBean(ServiceDiscovery.class);

                    List<String> addressList = null;
                    // 获取 RPC 服务地址
                    if (serviceDiscovery != null) {
                        String serviceName = interfaceName.getName();
                        if (StrUtil.isNotEmpty(version)) {
                            serviceName += "-" + version;
                        }
                        addressList = serviceDiscovery.discover(serviceName);
                        log.info("serviceName {} addressList {}",name,addressList);
                    }
                    if (CollectionUtil.isEmpty(addressList)) {
                        log.error("server address is empty");
                        throw new RuntimeException("server address is empty");
                    }
                    // 从 RPC 服务地址中解析主机名与端口号
                    for(String serviceAddress:addressList) {
                        log.info("serviceAddress {}",serviceAddress);
                        String[] array = StrUtil.split(serviceAddress, ":");
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);

                        ConnectManager connectManager = ConnectManager.getInstance();
                        List<String> servers = connectManager.getServers(name);
                        if(CollectionUtil.contains(servers,serviceAddress)){
                            //已开启对应服务
                            continue;
                        }
                        connectManager.addServer(name,serviceAddress);

                        RpcClient client = new RpcClient(host, port, name);
                        //启动连接
                        client.start();
                    }

                    // set bean
                    field.setAccessible(true);
                    field.set(bean, serviceProxy);


                }
            }
        });


        return super.postProcessAfterInstantiation(bean, beanName);
    }


}
