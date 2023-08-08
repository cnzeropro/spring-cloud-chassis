package org.zero.component.spring.boot.web.socket.stomp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.zero.common.data.model.common.Result;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * 发送消息主要有两种方式：
 * 1、Template模板方式（推荐）：灵活
 * 2、注解方式：方法的返回值则是要发送的消息
 *
 * @author zero
 * @since 2023/7/27
 */
@Slf4j
@RestController
@MessageMapping("/msg")
public class WsController {
    /* ***************************************************** 模板方式 ***************************************************** */
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 接收/app/msg/sendAll发来的消息并转发到/user/topic/all
     */
    @MessageMapping("/sendAll")
    public Result<Void> sendMsg2All(String msg) {
        simpMessagingTemplate.convertAndSend("/topic/all", Result.ok(msg));
        return Result.ok();
    }

    /**
     * 接收/app/msg/sendOne/{user}发来的消息并转发到/user/{user}/topic/one
     */
    @MessageMapping("/sendOne/{user}")
    public Result<Void> sendMsg2User(@DestinationVariable String user, @Payload String msg, @Header String token) {
        simpMessagingTemplate.convertAndSendToUser(user, "/topic/one", Result.ok(msg));
        return Result.ok();
    }

    /* ***************************************************** 注解方式 ***************************************************** */

    /**
     * 接收/app/msg/all发来的消息并转发到/user/topic/all
     */
    @MessageMapping("/all")
    @SendTo("/topic/all")
    public Result<Void> all(String msg, StompHeaderAccessor headerAccessor, Principal principal) {
        return Result.ok(msg);
    }

    /**
     * 接收/app/msg/one发来的消息并转发到/user/{user}/topic/callback
     * <p>
     * 注意：使用 @SendToUser 注解方式只会把消息发送给自己（或许可以用来做某些操作的异步解耦），如果想发送给指定用户，建议使用 Template API
     */
    @MessageMapping("/one")
    @SendToUser("/topic/callback")
    public Result<Void> one(String msg) {
        return Result.ok(msg);
    }

    /* ***************************************************** 订阅 ***************************************************** */

    /**
     * 当有客户端订阅该内容，会有一次性响应
     * <p>
     * 一般用于初始化数据，并不是真正的订阅
     */
    @SubscribeMapping("/subscribe")
    public Result<Void> subscribe() {
        return Result.ok("Thank you for subscribing to this channel");
    }

    /* ***************************************************** 异常处理 ***************************************************** */

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/topic/error")
    public Result<Void> handleException(Exception e) {
        log.error("Handling exception", e);
        return Result.error(e.getMessage());
    }
}
