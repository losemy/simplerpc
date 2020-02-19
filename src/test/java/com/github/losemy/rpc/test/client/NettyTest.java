package com.github.losemy.rpc.test.client;

import cn.hutool.core.lang.UUID;
import com.github.losemy.rpc.client.RpcClientHandler;
import com.github.losemy.rpc.common.bean.RpcRequest;
import com.github.losemy.rpc.common.bean.RpcResponse;
import com.github.losemy.rpc.common.codec.RpcDecoder;
import com.github.losemy.rpc.common.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lose
 * @date 2019-10-23
 **/
@Slf4j
public class NettyTest {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建并初始化 Netty 客户端 Bootstrap 对象
            Bootstrap bootstrap = new Bootstrap();
            RpcClientHandler rpcClientHandler = new RpcClientHandler();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    //pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                    pipeline.addLast(new RpcEncoder(RpcRequest.class)); // 编码 RPC 请求
                    pipeline.addLast(new RpcDecoder(RpcResponse.class)); // 解码 RPC 响应
                    pipeline.addLast(rpcClientHandler); // 处理 RPC 响应
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);

            String host = "127.0.0.1";
            int port = 18000;
            ChannelFuture future = bootstrap.connect(host, port).sync();

            //创建的连接之后需要共享

            Channel channel = future.channel();


            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        log.info("连接成功");
                    }
                }
            });
            RpcRequest request = new RpcRequest();
            request.setRequestId(UUID.fastUUID().toString(true));
            channel.writeAndFlush(request).sync();

            log.info("send request " + request.getRequestId());
        } catch(Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }
}
