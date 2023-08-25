package org.zero.demo.spring.boot.web.socket.tomcat;

import org.zero.common.data.model.Result;
import org.zero.common.data.util.spring.JacksonUtils;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @author zero
 * @since 2023/8/3
 */
public class JsonEncoder implements Encoder.Text<Result<Object>> {

    @Override
    public String encode(Result<Object> object) throws EncodeException {
        return JacksonUtils.toJsonStr(object);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
