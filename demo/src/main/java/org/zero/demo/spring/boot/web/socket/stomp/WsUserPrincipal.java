package org.zero.demo.spring.boot.web.socket.stomp;

import lombok.AllArgsConstructor;

import java.security.Principal;

/**
 * @author zero
 * @since 2023/8/3
 */
@AllArgsConstructor
public class WsUserPrincipal implements Principal {
    private String user;

    @Override
    public String getName() {
        return user;
    }
}
