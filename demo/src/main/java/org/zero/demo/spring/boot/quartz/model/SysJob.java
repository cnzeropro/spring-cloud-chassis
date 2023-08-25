package org.zero.demo.spring.boot.quartz.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author zero
 * @since 2023-07-24 10:02:12
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class SysJob implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 任务key
     */
    private String key;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 任务描述
     */
    private String description;
    /**
     * 任务组
     */
    private Long group;
    /**
     * 目标类
     */
    private String invokeTarget;
    /**
     * 目标方法
     */
    private String invokeMethod;
    /**
     * 参数（JSON）
     */
    private String parma;
    /**
     * 任务类型
     */
    private Integer type;
    /**
     * cron表达式
     */
    private String expression;
    /**
     * 状态（0：禁用，1：启用）
     */
    private Integer status;
}

