package org.zero.log.common.event;

import org.springframework.context.ApplicationEvent;
import org.zero.log.common.model.SysLogPO;

/**
 * 系统日志事件
 *
 * @author zero
 * @date 2022/1/5
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLogPO source) {
        super(source);
    }

    @Override
    public SysLogPO getSource() {
        return (SysLogPO) source;
    }
}
