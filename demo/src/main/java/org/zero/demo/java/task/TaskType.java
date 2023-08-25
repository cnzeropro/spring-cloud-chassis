package org.zero.demo.java.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yufa.wang (yufa.wang@ronganchina.com)
 * @since 2023/3/29
 */
@Getter
@RequiredArgsConstructor
public enum TaskType {
    TRIGGERED_TASK(1, "触发任务"),
    SCHEDULED_TASK(2, "定时任务"),
    ;

    private final int code;
    private final String name;
}
