package org.zero.demo.java.task.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yufa.wang (yufa.wang@ronganchina.com)
 * @since 2023/3/29
 */
@Getter
@RequiredArgsConstructor
public enum TaskType {
    /**
     * 触发任务
     */
    TRIGGERED_TASK,
    /**
     * 定时任务
     */
    SCHEDULED_TASK,
    /**
     * 无任务
     */
    NONE,
    ;
}
