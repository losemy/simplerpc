package com.github.losemy.rpc.loadbalance;

/**
 * @author lose
 * @date 2019-11-11
 **/
public class LoadBalanceFactory {

    public static LoadBalance getLoadBalanceByName(String name){
        LoadBalance loadBalance = null;
        switch (name){
            default:
                loadBalance = new RandomLoadBalance();
        }
        return loadBalance;
    }
}
