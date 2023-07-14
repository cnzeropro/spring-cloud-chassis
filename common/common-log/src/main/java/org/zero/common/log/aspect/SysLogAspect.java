/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zero.common.log.aspect;

import cn.hutool.extra.spring.SpringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;
import org.zero.common.data.model.po.SysLogPO;
import org.zero.common.log.annotation.SysLog;
import org.zero.common.log.event.SysLogEvent;
import org.zero.common.log.constant.LogTypeEnum;
import org.zero.common.log.util.SysLogUtil;

/**
 * 操作日志使用spring event异步入库
 *
 * @author zero
 * @date 2022/1/3
 */
@Aspect
@Slf4j
public class SysLogAspect {

    @Around("@annotation(sysLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, SysLog sysLog) {
        String className = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        log.debug("ClassName: {}, MethodName: {}", className, methodName);

        String value = sysLog.value();
        String expression = sysLog.expression();
        // 当前 SpEL 存在，会覆盖 value 的值
        if (StringUtils.hasText(expression)) {
            MethodSignature signature = (MethodSignature) point.getSignature();
            try {
                value = SysLogUtil.getValue(signature.getMethod(), point.getArgs(), expression, String.class);
            } catch (Exception e) {
                log.error(String.format("@SysLog parse SpEL[%s] error", expression), e);
            }
        }

        SysLogPO log = SysLogUtil.getSysLog();
        log.setTitle(value);

        // 发送异步日志事件
        Object result;
        Long startTime = System.currentTimeMillis();
        try {
            result = point.proceed();
        } catch (Exception e) {
            log.setType(LogTypeEnum.ERROR.getType());
            log.setException(e.getMessage());
            throw e;
        } finally {
            Long endTime = System.currentTimeMillis();
            log.setTime(endTime - startTime);
            SpringUtil.publishEvent(new SysLogEvent(log));
        }

        return result;
    }
}
