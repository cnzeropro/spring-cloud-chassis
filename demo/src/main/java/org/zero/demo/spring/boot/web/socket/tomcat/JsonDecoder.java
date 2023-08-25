package org.zero.demo.spring.boot.web.socket.tomcat;

import org.zero.common.data.util.spring.JacksonUtils;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.Objects;

/**
 * @author zero
 * @since 2023/8/3
 */
public class JsonDecoder implements Decoder.Text<CustomPojo> {
    @Override
    public CustomPojo decode(String s) throws DecodeException {
        return JacksonUtils.toObj(s, CustomPojo.class);
    }

    @Override
    public boolean willDecode(String s) {
        return Objects.nonNull(s);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
