package com.github.losemy.rpc.client;

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
public class RpcClient {

    private final String host;
    private final int port;
    private final String name;

    public RpcClient(String host, int port, String name) {
        this.host = host;
        this.port = port;
        this.name = name;
    }

    public void start(){

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

            log.info("host{}:port{}", host , port);
            ChannelFuture future = bootstrap.connect(host, port).sync();

            //创建的连接之后需要共享

            Channel channel = future.channel();

            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        log.info("连接成功");
                        ConnectManager.getInstance().addRpcClientHandler(rpcClientHandler,name);
                    }
                }
            });
        } catch(Exception e){
            log.error("开启连接异常",e);
        }
    }

}
