package com.github.losemy.rpc.register.zk.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.List;

/**
 * @author lose
 * @date 2019-11-11
 **/
public class RandomLoadBalance implements LoadBalance {

    @Override
    public String loadBalance(List<String> serverAddress) {
        if(CollUtil.isNotEmpty(serverAddress)) {
            return serverAddress.get(RandomUtil.randomInt(serverAddress.size()));
        }
        return null;
    }
}
