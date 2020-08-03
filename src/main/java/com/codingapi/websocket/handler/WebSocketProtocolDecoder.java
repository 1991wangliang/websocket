package com.codingapi.websocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.websocket.event.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
@Slf4j
public class WebSocketProtocolDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        String data = msg.text();
        JSONObject jsonObject = JSONObject.parseObject(data);

        Message message = new Message();
        message.setAction(jsonObject.getString("action"));
        message.setData(jsonObject.getString("data"));
        out.add(message);
    }
}
