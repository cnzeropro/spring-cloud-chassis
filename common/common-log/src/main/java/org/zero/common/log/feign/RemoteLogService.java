package org.zero.common.log.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zero.common.data.model.Result;
import org.zero.common.log.model.SysLogPO;

/**
 * @author zero
 * @since 2021/7/13
 */
@FeignClient(contextId = "remoteLogService", name = "")
public interface RemoteLogService {
    /**
     * 保存日志
     */
    @PostMapping("/log")
    Result<Boolean> save(@RequestBody SysLogPO sysLog);
}