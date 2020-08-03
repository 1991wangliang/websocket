package com.codingapi.websocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.websocket.event.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
public class WebSocketProtocolEncoder extends MessageToMessageEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action",msg.getAction());
        jsonObject.put("data",msg.getData());
        out.add(new TextWebSocketFrame(jsonObject.toJSONString()));
    }
}
