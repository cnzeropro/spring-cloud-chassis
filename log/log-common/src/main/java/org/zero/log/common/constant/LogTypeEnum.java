package org.zero.log.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 日志类型
 *
 * @author zero
 * @date 2021/7/30
 */
@Getter
@RequiredArgsConstructor
public enum LogTypeEnum {
    /**
     * 正常日志类型
     */
    NORMAL("1", "正常日志"),
    /**
     * 错误日志类型
     */
    ERROR("2", "错误日志"),
    ;

    /**
     * 类型
     */
    private final String type;
    /**
     * 描述
     */
    private final String description;
}
