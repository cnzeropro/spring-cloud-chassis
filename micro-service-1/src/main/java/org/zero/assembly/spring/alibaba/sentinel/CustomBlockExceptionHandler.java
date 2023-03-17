package org.zero.assembly.spring.alibaba.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.zero.model.vo.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author Zero
 * @since 2022/7/16
 */
@Slf4j
@Component
public class CustomBlockExceptionHandler implements BlockExceptionHandler {
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        Result<String> result = Result.error("未知异常", e.getMessage());
        if (e instanceof FlowException) {
            result = Result.error("接口已被限流", e.getMessage());
        }
        if (e instanceof DegradeException) {
            result = Result.error("服务已被降级", e.getMessage());
        }
        if (e instanceof ParamFlowException) {
            result = Result.error("热点参数被限流", e.getMessage());
        }
        if (e instanceof SystemBlockException) {
            result = Result.error("触发系统保护规则", e.getMessage());
        }
        if (e instanceof AuthorityException) {
            result = Result.error("未被授权，请稍后再试", e.getMessage());
        }

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), result);
    }
}
