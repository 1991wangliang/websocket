package com.codingapi.websocket;

import com.codingapi.websocket.handler.WebSocketHandler;
import com.codingapi.websocket.handler.WebSocketProtocolDecoder;
import com.codingapi.websocket.handler.WebSocketProtocolEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
@Component
@Slf4j
public class NettyServer {

    private int port = 8800;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @SneakyThrows
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            sb.group(group, bossGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            ch.pipeline().addLast(new HttpObjectAggregator(8192));
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket",
                                    "websocket", true, 65536 * 10));
                            ch.pipeline().addLast(new WebSocketProtocolDecoder());
                            ch.pipeline().addLast(new WebSocketProtocolEncoder());
                            ch.pipeline().addLast(webSocketHandler);
                        }
                    });

            ChannelFuture cf = sb.bind(port);
            log.info("websocket bind:{}",port);
            cf.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
        }
    }
}
