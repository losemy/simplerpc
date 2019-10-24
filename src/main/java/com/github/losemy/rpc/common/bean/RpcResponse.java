package com.github.losemy.rpc.common.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lose
 * @date 2019-10-23
 **/
@Getter
@Setter
public class RpcResponse {
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 异常
     */
    private Exception exception;
    /**
     * 返回数据
     */
    private Object result;
}
