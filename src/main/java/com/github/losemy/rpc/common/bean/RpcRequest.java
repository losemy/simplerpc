package com.github.losemy.rpc.common.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lose
 * @date 2019-10-23
 **/
@Getter
@Setter
public class RpcRequest {
    /**
     * 唯一请求ID
     */
    private String requestId;
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 接口版本
     */
    private String serviceVersion;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 参数
     */
    private Object[] parameters;
}
