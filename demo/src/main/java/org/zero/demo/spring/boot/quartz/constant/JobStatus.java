package org.zero.demo.spring.boot.quartz.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zero
 * @since 2023/10/17
 */
@Getter
@RequiredArgsConstructor
public enum JobStatus {
    /**
     * 停用
     */
    DISABLE(0),
    /**
     * 启用
     */
    ENABLE(1),
    ;

    private final Integer status;
}
