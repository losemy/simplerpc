package com.github.losemy.rpc.register;

/**
 * @author lose
 * @date 2019-11-12
 **/
public interface ServiceAutoDiscovery {
    /**
     * 监听服务上下线实现 服务自动发现与注销
     * @param serviceName
     */
    void autoDiscovery(String serviceName);

}
