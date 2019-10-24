package com.github.losemy.rpc.register;

import java.util.List;

/**
 * 服务发现接口
 *
 * @author lose
 * @date 2019-10-23
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名称查找服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    List<String> discover(String serviceName);
}