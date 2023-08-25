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
public class SysJobGroup implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 任务组key
     */
    private String key;
    /**
     * 任务组名称
     */
    private String name;
    /**
     * 任务组描述
     */
    private String description;
}

