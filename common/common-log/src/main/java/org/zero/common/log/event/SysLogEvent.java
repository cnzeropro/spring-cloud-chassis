package org.zero.common.log.event;

import org.springframework.context.ApplicationEvent;
import org.zero.common.api.basic.log.model.SysLogDTO;

/**
 * 系统日志事件
 *
 * @author zero
 * @date 2022/1/5
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLogDTO source) {
        super(source);
    }

    @Override
    public SysLogDTO getSource() {
        return (SysLogDTO) source;
    }
}
