package org.zero.basic.api.log;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zero.basic.api.log.model.SysLogDTO;
import org.zero.common.data.model.vo.Result;

/**
 * @author zero
 * @since 2021/7/13
 */
@FeignClient(name = "log-api", contextId = "remoteLogService", fallbackFactory = RemoteLogFeignFallbackFactory.class)
public interface RemoteLogService {
    /**
     * 保存日志
     */
    @PostMapping("/log")
    Result<Boolean> save(@RequestBody SysLogDTO sysLog);
}
