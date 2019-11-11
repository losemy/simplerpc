package com.github.losemy.rpc.client;

import com.alibaba.fastjson.JSON;
import com.github.losemy.rpc.common.bean.RpcRequest;
import com.github.losemy.rpc.common.bean.RpcResponse;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lose
 * @date 2019-10-23
 **/
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private ConcurrentHashMap<String, CompletableFuture<RpcResponse>> responses = new ConcurrentHashMap<>();

    private volatile Channel channel;

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        String requestId = response.getRequestId();
        log.info("requestId {}", requestId);
        //异步处理消息返回，有消息就更新 然后get才能够获取到
        CompletableFuture<RpcResponse> rpcFuture = responses.get(requestId);
        if (rpcFuture != null) {
            responses.remove(requestId);
            rpcFuture.complete(response);
            log.info("read done {}" , rpcFuture.isDone());
        }else{
            log.error("没有对应的CompletableFuture");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("api caught exception", cause);
        ctx.close();
    }

    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest){
        try {
            log.info("sendRequest {}" , JSON.toJSONString(rpcRequest));
            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();

            responses.put(rpcRequest.getRequestId(), responseFuture);
            //发送请求
            channel.writeAndFlush(rpcRequest).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    log.info("发送请求 " + rpcRequest.getRequestId());
                }
            });
            log.info("isActive {}", channel.isActive());
            return responseFuture;
        }catch(Exception e){
            log.info(e.getMessage());
        }
        return null;
    }
}
