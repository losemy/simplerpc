package com.github.losemy.rpc.register.zk.strategy;

import java.util.List;

/**
 * @author lose
 * @date 2019-11-11
 **/
public interface LoadBalance {

    /**
     * 负载均衡
     * @param serverAddress
     * @return
     */
    String loadBalance(List<String> serverAddress);
}
