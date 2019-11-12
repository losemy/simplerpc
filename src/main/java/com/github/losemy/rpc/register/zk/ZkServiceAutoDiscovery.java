package com.github.losemy.rpc.register.zk;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.github.losemy.rpc.register.ServiceAutoDiscovery;
import com.github.losemy.rpc.util.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @author lose
 * @date 2019-11-12
 **/
@Slf4j
public class ZkServiceAutoDiscovery implements ServiceAutoDiscovery {

    @Autowired
    private ZkUtil zkUtil;

    private Set<String> servicePaths = new ConcurrentHashSet<>();

    @Override
    public void autoDiscovery(String serviceName) {
        if(!CollUtil.contains(servicePaths,serviceName)){
            if(zkUtil.monitorServicePath(serviceName)) {
                servicePaths.add(serviceName);
            }
        }else{
            log.info("ready watching {}",serviceName);
        }
    }


}
