package com.github.losemy.rpc.client;

import cn.hutool.core.util.StrUtil;

/**
 * @author lose
 * @date 2019-10-23
 **/
public class ServiceUtil {

    public static String buildServiceName(String interfaceName, String version){
        String serviceName = interfaceName;
        if(StrUtil.isNotEmpty(version)){
            serviceName += version;
        }
        return serviceName;
    }
}
