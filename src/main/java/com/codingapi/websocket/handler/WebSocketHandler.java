package com.codingapi.websocket.handler;

import com.codingapi.websocket.event.Message;
import com.codingapi.websocket.event.MessageEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
@Slf4j
@AllArgsConstructor
@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<Message> {

    private List<MessageEvent> messageEvents;

    private ChannelGroup channels;

    private Executor executor;

    @Autowired(required = false)
    public WebSocketHandler(List<MessageEvent> messageEvents) {
        this.messageEvents = messageEvents;
        this.channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        log.info("read msg:{}",message);
        if(messageEvents!=null) {
            for (MessageEvent event : messageEvents) {
                if (event.action().equals(message.getAction())) {
                    executor.execute(()->{
                        event.invoke((msg)->{
                            sendMessage(ctx.channel(),msg);
                        }, message);
                    });
                }
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        channels.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        channels.remove(ctx.channel());
    }

    private void sendMessage(Channel channel,Message message){
        log.info("send msg:{}",message);
        channel.writeAndFlush(message);
    }
}
