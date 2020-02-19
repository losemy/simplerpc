package com.github.losemy.rpc.util;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;

/**
 * @author lose
 * @date 2019-11-13
 **/
@Slf4j
public class MyZkSerializer implements ZkSerializer {

    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {
        try {
            return String.valueOf(data).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("zk序列化失败",e);
        }
        return null;
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("zk反序列化失败",e);
        }
        return null;
    }
}
