package org.zero.component.spring.boot.web.socket.stomp;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zero.common.data.model.common.Result;

import javax.annotation.Resource;

/**
 * @author zero
 * @since 2023/7/27
 */
@RestController
public class WsController {
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    /* ************************************** 全局发送 ************************************** */

    @MessageMapping("/all")
    @SendTo("/topic/all")
    public String all(String msg) {
        return msg;
    }

    @GetMapping("/template/all")
    public Result<Void> sendMsg2All(String msg) {
        simpMessagingTemplate.convertAndSend("/topic/all", msg);
        return Result.ok();
    }

    /* ************************************** 单点发送 ************************************** */

    @MessageMapping("/one/{user}")
    @SendToUser("/topic/one")
    public String one(@DestinationVariable("user") String user, String msg) {
        return msg;
    }

    @GetMapping("/template/one")
    public Result<Void> sendMsg2User(String user, String msg) {
        simpMessagingTemplate.convertAndSendToUser(user, "/topic/one", msg);
        return Result.ok();
    }

    /* ************************************** 订阅 ************************************** */

    /**
     * 当有客户端订阅该内容，会有一次性响应
     * <p>
     * 一般用于初始化数据，并不是真正的订阅
     */
    @SubscribeMapping("/subscribe")
    public Result<Void> subscribe() {
        return Result.ok("Thank you for subscribing to this channel");
    }
}
