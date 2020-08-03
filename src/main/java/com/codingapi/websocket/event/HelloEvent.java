package com.codingapi.websocket.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
@Component
@Slf4j
public class HelloEvent implements MessageEvent {

    @Override
    public String action() {
        return "hello";
    }

    @Override
    public void invoke(Response response, Message message) {
        log.info("hello:{}",message);
        response.send(message);
    }
}
