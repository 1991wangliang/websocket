package com.codingapi.websocket.event;

/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
public interface MessageEvent {

    String action();

    void invoke(Response response, Message message);

}
